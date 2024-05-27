package com.adman.craft.comments_mgmt.service;

import com.adman.craft.comments_mgmt.representation.AuthRequest;
import com.adman.craft.comments_mgmt.representation.AuthResponse;
import com.adman.craft.comments_mgmt.representation.RegistrationRequest;
import com.adman.craft.comments_mgmt.data.entity.SocialUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AuthService {
    AuthResponse register(RegistrationRequest request);

    AuthResponse authenticate(AuthRequest request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    AuthResponse authResponse();

    SocialUser getCurrentUser();
}
