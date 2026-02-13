package com.translation.speech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for speech-to-text response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeechToTextResponse {
    private String text;
    private String language;
    private Integer confidenceScore;
    private Long durationMs;
}
