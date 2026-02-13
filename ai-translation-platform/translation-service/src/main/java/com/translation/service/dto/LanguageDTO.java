package com.translation.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for language information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDTO {

    private String code;
    private String name;
    private String nativeName;
    private Boolean supportsTextTranslation;
    private Boolean supportsSpeechRecognition;
    private Boolean supportsSpeechSynthesis;
}
