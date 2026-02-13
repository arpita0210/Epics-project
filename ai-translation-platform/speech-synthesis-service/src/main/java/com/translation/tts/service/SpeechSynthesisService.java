package com.translation.tts.service;

import com.translation.common.exception.TranslationException;
import com.translation.tts.dto.TextToSpeechRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechSynthesisService {

    private final RestTemplate restTemplate;

    @Value("${ml.tts.endpoint:http://localhost:8000/text-to-speech}")
    private String ttsEndpoint;

    /**
     * Convert text to speech
     */
    public byte[] textToSpeech(TextToSpeechRequest request) {
        log.info("Converting text to speech, language: {}", request.getLanguage());

        try {
            return callTTSAPI(request);
        } catch (Exception e) {
            log.error("Error in text-to-speech conversion", e);
            return getMockAudio(request);
        }
    }

    /**
     * Call TTS API (Tacotron 2 or similar)
     */
    private byte[] callTTSAPI(TextToSpeechRequest request) {
        try {
            Map<String, Object> ttsRequest = new HashMap<>();
            ttsRequest.put("text", request.getText());
            ttsRequest.put("language", request.getLanguage());
            ttsRequest.put("voice", request.getVoice() != null ? request.getVoice() : "default");
            ttsRequest.put("speed", request.getSpeed() != null ? request.getSpeed() : 1.0f);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(ttsRequest, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    ttsEndpoint,
                    HttpMethod.POST,
                    entity,
                    byte[].class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new TranslationException("TTS API returned unexpected response");
            }
        } catch (Exception e) {
            log.error("Error calling TTS API", e);
            throw new TranslationException("Failed to call TTS API", e);
        }
    }

    /**
     * Mock audio generation for demo/testing
     */
    private byte[] getMockAudio(TextToSpeechRequest request) {
        log.warn("Using mock TTS - TTS API unavailable");
        // Return empty audio data as placeholder
        String mockData = "MOCK_AUDIO_DATA_FOR_TEXT: " + request.getText();
        return mockData.getBytes();
    }
}
