package com.adman.craft.comments_mgmt.controller;

import com.adman.craft.comments_mgmt.data.Role;
import com.adman.craft.comments_mgmt.errorhandling.exception.ExternalRuntimeException;
import com.adman.craft.comments_mgmt.representation.AuthRequest;
import com.adman.craft.comments_mgmt.representation.AuthResponse;
import com.adman.craft.comments_mgmt.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@SpringBootTest
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testAuthenticate_validRequest_returnsOk() throws Exception {
        // Mock AuthService behavior
        AuthRequest request = new AuthRequest("username", "password");
        AuthResponse response = new AuthResponse(0L, true,
                Role.ADMIN, Role.ADMIN.getPermissions(), "access_token", "refresh_token");
        when(authService.authenticate(request)).thenReturn(response);

        // Call the controller method
        ResponseEntity<AuthResponse> actualResponse = authController.authenticate(request);

        // Verify response
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(response, actualResponse.getBody());
    }

    @Test
    public void testAuthenticate_invalidRequest_throwsException() throws Exception {
        // Mock AuthService behavior (throwing exception)
        AuthRequest request = new AuthRequest(null, null);
        when(authService.authenticate(request)).thenThrow(ExternalRuntimeException.class);

        // Call the controller method
        assertThrows(ExternalRuntimeException.class, () -> authController.authenticate(request));
    }
}

