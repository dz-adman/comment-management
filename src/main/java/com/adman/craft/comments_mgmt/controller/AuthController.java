package com.adman.craft.comments_mgmt.controller;

import com.adman.craft.comments_mgmt.representation.AuthRequest;
import com.adman.craft.comments_mgmt.representation.AuthResponse;
import com.adman.craft.comments_mgmt.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Tag(name = "Authentication", description = "Authentication management related operations (APIs)")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Get Auth Response",
            description = "This API can be used to Authenticate User and get the AuthResponse that includes tokens and other details",
            tags = "Authentication"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @Operation(
            summary = "Refresh Auth-Token",
            description = "This API can be used to Refresh the Auth-Token",
            tags = "Authentication"
    )
    @GetMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @Operation(
            summary = "Validate Auth-Token",
            description = "This API can be used to Validate the Auth-Token",
            tags = "Authentication"
    )
    @GetMapping("/validate-token")
    public ResponseEntity<AuthResponse> validateToken() {
        return ResponseEntity.ok(authService.authResponse());
    }

}
