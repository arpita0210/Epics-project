package com.translation.tts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextToSpeechRequest {

    @NotBlank(message = "Text is required")
    @Size(max = 5000, message = "Text must not exceed 5000 characters")
    private String text;

    @NotBlank(message = "Language is required")
    private String language;

    private String voice; // Optional voice selection
    private Float speed; // Speech speed (0.5 to 2.0)
}
