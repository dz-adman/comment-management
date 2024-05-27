package com.adman.craft.comments_mgmt.controller;

import com.adman.craft.comments_mgmt.representation.AuthResponse;
import com.adman.craft.comments_mgmt.representation.RegistrationRequest;
import com.adman.craft.comments_mgmt.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Registration", description = "Registration management related operations (APIs)")
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final AuthService authService;

    @Operation(
            summary = "User Registration",
            description = "This API provides functionality for new user registration to the application. This API does not require authentication.",
            tags = "Registration"
    )
    @PostMapping
    public ResponseEntity<AuthResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

}
