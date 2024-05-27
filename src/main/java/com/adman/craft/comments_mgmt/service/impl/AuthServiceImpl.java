package com.adman.craft.comments_mgmt.service.impl;

import com.adman.craft.comments_mgmt.data.TokenType;
import com.adman.craft.comments_mgmt.data.entity.LoginDetails;
import com.adman.craft.comments_mgmt.data.entity.RefreshToken;
import com.adman.craft.comments_mgmt.data.entity.Token;
import com.adman.craft.comments_mgmt.data.repository.LoginDetailsRepository;
import com.adman.craft.comments_mgmt.data.repository.RefreshTokenRepository;
import com.adman.craft.comments_mgmt.data.repository.TokenRepository;
import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import com.adman.craft.comments_mgmt.errorhandling.exception.UserAlreadyExistsException;
import com.adman.craft.comments_mgmt.representation.AuthRequest;
import com.adman.craft.comments_mgmt.representation.AuthResponse;
import com.adman.craft.comments_mgmt.representation.RegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adman.craft.comments_mgmt.data.entity.SocialUser;
import com.adman.craft.comments_mgmt.data.repository.SocialUserRepository;
import com.adman.craft.comments_mgmt.service.AuthService;
import com.adman.craft.comments_mgmt.service.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final LoginDetailsRepository loginDetailsRepo;
    private final SocialUserRepository userRepo;
    private final TokenRepository tokenRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public AuthResponse register(RegistrationRequest request) {
        log.info("processing user registration request for {}", request);

        Optional<LoginDetails> optionalLoginDetails = loginDetailsRepo.findByLoginId(request.loginId());

        if (optionalLoginDetails.isPresent()) {
            log.warn("user already exists with these details");
            throw new UserAlreadyExistsException(
                    ErrorCode.USER_ALREADY_EXISTS, "User Already Present with these details");
        }

        // build and save user
        var user = SocialUser.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .createdAt(LocalDateTime.now())
                .build();
        userRepo.save(user);

        // build and save loginDetails
        LoginDetails loginDetails = LoginDetails.builder()
                .user(user)
                .loginId(request.loginId())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .enabled(true)
                .locked(false)
                .deleted(false)
                .build();
        loginDetailsRepo.save(loginDetails);

        // return auth response
        return AuthResponse.builder()
                .userId(user.getId())
                .isAuthenticated(true)
                .role(loginDetails.getRole())
                .permissions(loginDetails.getRole().getPermissions())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse authenticate(AuthRequest request) {
        log.info("processing auth request");

        // use authenticationProvider to authenticate (automatically fails if un-authenticated)
        Authentication authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        log.info("auth successful");

        // reached here ? auth successful
        LoginDetails loginDetails = (LoginDetails) authentication.getPrincipal();

        // generate tokens
        log.info("generating tokens");
        var jwtToken = jwtService.generateToken(loginDetails);
        var refreshToken = jwtService.generateRefreshToken(loginDetails);
        RefreshToken rt = saveRefreshToken(loginDetails.getUser(), refreshToken);

        // save tokens
        log.info("saving tokens");
        saveJwtToken(loginDetails.getUser(), jwtToken, rt);

        // return auth response with tokens
        return AuthResponse.builder()
                .userId(loginDetails.getUser().getId())
                .isAuthenticated(true)
                .role(loginDetails.getRole())
                .permissions(loginDetails.getRole().getPermissions())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveJwtToken(SocialUser user, String jwtToken, RefreshToken refreshToken) {
        var now = Instant.now();
        var jwt = Token.builder()
                .user(user)
                .token(jwtToken)
                .refreshToken(refreshToken)
                .validFrom(now)
                .validTill(now.plusMillis(jwtExpiration))
                .build();
        tokenRepo.save(jwt);
    }

    private RefreshToken saveRefreshToken(SocialUser user, String refreshToken) {
        var now = Instant.now();
        var refresh = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .validFrom(now)
                .validTill(now.plusMillis(refreshExpiration))
                .build();
        return refreshTokenRepo.save(refresh);
    }

    @Override
    @Transactional
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // check for bearer token
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        final String refreshToken = authHeader.substring(7);
        final String username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var loginDetails = loginDetailsRepo.findByLoginId(username).orElseThrow();
            // validate refresh token and proceed
            if (jwtService.isTokenValid(refreshToken, TokenType.BEARER_REFRESH, loginDetails)) {
                // generate new tokens
                log.info("generating new tokens");
                var accessToken = jwtService.generateToken(loginDetails);
                // delete existing tokens
                log.info("deleting existing tokens");
                RefreshToken rt = refreshTokenRepo.findByToken(refreshToken).orElse(null);
                deleteAllJwtTokensForUser(loginDetails.getUser(), rt);
                // save new tokens
                log.info("saving tokens");
                saveJwtToken(loginDetails.getUser(), accessToken, rt);
                // return auth response with tokens
                var authResponse = AuthResponse.builder()
                        .userId(loginDetails.getUser().getId())
                        .isAuthenticated(true)
                        .role(loginDetails.getRole())
                        .permissions(loginDetails.getRole().getPermissions())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                response.setHeader("content-type", "application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    @Transactional
    public AuthResponse authResponse() {
        // success if we get auth principal from securityContextHolder
        LoginDetails loginDetails = (LoginDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return AuthResponse.builder()
                .userId(loginDetails.getUser().getId())
                .isAuthenticated(true)
                .role(loginDetails.getRole())
                .permissions(loginDetails.getRole().getPermissions())
                .build();
    }

    @Override
    public @Nonnull SocialUser getCurrentUser() {
        LoginDetails loginDetails = (LoginDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginDetails.getUser();
    }

    private void deleteAllJwtTokensForUser(SocialUser user, RefreshToken refreshToken) {
        var userTokens = tokenRepo.findAllByUserAndRefreshToken(user, refreshToken);
        if (userTokens.isEmpty()) return;
        userTokens.forEach(tokenRepo::delete);
    }

}
