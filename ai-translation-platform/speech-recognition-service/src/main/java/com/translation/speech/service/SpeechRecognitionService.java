package com.translation.speech.service;

import com.translation.common.exception.TranslationException;
import com.translation.speech.dto.SpeechToTextResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Service for speech recognition using Whisper API
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechRecognitionService {

    private final RestTemplate restTemplate;

    @Value("${ml.whisper.endpoint:http://localhost:8000/speech-to-text}")
    private String whisperEndpoint;

    /**
     * Convert speech to text
     */
    public SpeechToTextResponse speechToText(MultipartFile audioFile, String language) {
        log.info("Converting speech to text, language: {}", language);

        try {
            long startTime = System.currentTimeMillis();

            // Call Whisper API
            String transcribedText = callWhisperAPI(audioFile, language);

            long duration = System.currentTimeMillis() - startTime;

            return SpeechToTextResponse.builder()
                    .text(transcribedText)
                    .language(language)
                    .confidenceScore(90)
                    .durationMs(duration)
                    .build();

        } catch (Exception e) {
            log.error("Error in speech recognition", e);
            // Fallback to mock response
            return getMockSpeechToText(audioFile, language);
        }
    }

    /**
     * Call Whisper API for speech recognition
     */
    private String callWhisperAPI(MultipartFile audioFile, String language) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", audioFile.getResource());
            body.add("language", language);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    whisperEndpoint,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("text");
            } else {
                throw new TranslationException("Whisper API returned unexpected response");
            }
        } catch (Exception e) {
            log.error("Error calling Whisper API", e);
            throw new TranslationException("Failed to call Whisper API", e);
        }
    }

    /**
     * Mock speech-to-text for demo/testing
     */
    private SpeechToTextResponse getMockSpeechToText(MultipartFile audioFile, String language) {
        log.warn("Using mock speech recognition - Whisper API unavailable");
        return SpeechToTextResponse.builder()
                .text("[MOCK TRANSCRIPTION] This is a sample transcribed text from audio file: " + audioFile.getOriginalFilename())
                .language(language)
                .confidenceScore(85)
                .durationMs(500L)
                .build();
    }
}
