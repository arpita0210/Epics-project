package com.translation.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for translation responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslationResponse {

    private String translatedText;
    private String sourceLanguage;
    private String targetLanguage;
    private Integer confidenceScore;
    private String modelVersion;
    private LocalDateTime timestamp;
    private Boolean fromCache;
}
