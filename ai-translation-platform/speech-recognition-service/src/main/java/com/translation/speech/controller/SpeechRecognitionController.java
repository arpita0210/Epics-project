package com.translation.speech.controller;

import com.translation.common.dto.ApiResponse;
import com.translation.speech.dto.SpeechToTextResponse;
import com.translation.speech.service.SpeechRecognitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller for speech recognition
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Speech Recognition", description = "Speech-to-text API endpoints")
public class SpeechRecognitionController {

    private final SpeechRecognitionService speechRecognitionService;

    /**
     * Convert speech to text
     */
    @PostMapping(value = "/speech-to-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Convert speech to text",
        description = "Upload an audio file and convert it to text using Whisper API"
    )
    public ResponseEntity<ApiResponse<SpeechToTextResponse>> speechToText(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "language", defaultValue = "en") String language) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Audio file is required"));
        }

        SpeechToTextResponse response = speechRecognitionService.speechToText(file, language);
        return ResponseEntity.ok(ApiResponse.success("Speech converted to text successfully", response));
    }
}
