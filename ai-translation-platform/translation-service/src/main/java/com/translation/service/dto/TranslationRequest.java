package com.translation.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for translation requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslationRequest {

    @NotBlank(message = "Source text is required")
    @Size(max = 10000, message = "Text must not exceed 10000 characters")
    private String text;

    @NotBlank(message = "Source language is required")
    @Size(min = 2, max = 10, message = "Language code must be between 2 and 10 characters")
    private String sourceLanguage;

    @NotBlank(message = "Target language is required")
    @Size(min = 2, max = 10, message = "Language code must be between 2 and 10 characters")
    private String targetLanguage;

    private Boolean saveToHistory = true;
}
