package com.translation.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for user information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String preferredLanguage;
    private Boolean active;
    private Boolean emailVerified;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
