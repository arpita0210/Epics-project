package com.translation.user.controller;

import com.translation.common.dto.ApiResponse;
import com.translation.user.dto.*;
import com.translation.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication and user management
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and management endpoints")
public class AuthController {

    private final UserService userService;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    @Operation(
        summary = "Register new user",
        description = "Create a new user account"
    )
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = userService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", user));
    }

    /**
     * Login user
     */
    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticate user and receive JWT token"
    )
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    /**
     * Get current user profile
     */
    @GetMapping("/profile")
    @Operation(
        summary = "Get user profile",
        description = "Get the profile of the currently authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication required"));
        }

        String username = authentication.getName();
        UserDTO user = userService.getUserProfile(username);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * Refresh JWT token
     */
    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh token",
        description = "Refresh the JWT access token",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> refreshToken(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication required"));
        }

        // In a real implementation, you would validate the refresh token
        // and generate a new access token
        return ResponseEntity.ok(ApiResponse.success("Token refresh not yet implemented"));
    }
}
