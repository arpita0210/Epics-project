package com.translation.tts.controller;

import com.translation.common.dto.ApiResponse;
import com.translation.tts.dto.TextToSpeechRequest;
import com.translation.tts.service.SpeechSynthesisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Speech Synthesis", description = "Text-to-speech API endpoints")
public class SpeechSynthesisController {

    private final SpeechSynthesisService speechSynthesisService;

    /**
     * Convert text to speech
     */
    @PostMapping("/text-to-speech")
    @Operation(
        summary = "Convert text to speech",
        description = "Generate audio from text using TTS models"
    )
    public ResponseEntity<byte[]> textToSpeech(@Valid @RequestBody TextToSpeechRequest request) {
        byte[] audioData = speechSynthesisService.textToSpeech(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "speech.wav");

        return ResponseEntity.ok()
                .headers(headers)
                .body(audioData);
    }

    /**
     * Get available voices
     */
    @GetMapping("/voices")
    @Operation(
        summary = "Get available voices",
        description = "Retrieve list of available TTS voices"
    )
    public ResponseEntity<ApiResponse<String[]>> getVoices() {
        String[] voices = {"default", "male", "female", "child"};
        return ResponseEntity.ok(ApiResponse.success(voices));
    }
}
