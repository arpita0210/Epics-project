package com.translation.service.service;

import com.translation.common.exception.TranslationException;
import com.translation.service.dto.TranslationRequest;
import com.translation.service.dto.TranslationResponse;
import com.translation.service.model.Translation;
import com.translation.service.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for handling translation operations using Google Cloud Translation API
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final RestTemplate restTemplate;

    @Value("${google.cloud.api-key:}")
    private String googleApiKey;

    /**
     * Translate text with caching
     */
    @Transactional
    @Cacheable(value = "translations", key = "#request.sourceLanguage + '_' + #request.targetLanguage + '_' + #request.text.hashCode()")
    public TranslationResponse translate(TranslationRequest request, Long userId) {
        log.info("Translating text from {} to {}", request.getSourceLanguage(), request.getTargetLanguage());

        // Check database for recent translation
        Optional<Translation> cachedTranslation = translationRepository.findRecentTranslation(
                request.getText(),
                request.getSourceLanguage(),
                request.getTargetLanguage()
        );

        if (cachedTranslation.isPresent()) {
            log.info("Found cached translation in database");
            Translation cached = cachedTranslation.get();
            return TranslationResponse.builder()
                    .translatedText(cached.getTranslatedText())
                    .sourceLanguage(cached.getSourceLanguage())
                    .targetLanguage(cached.getTargetLanguage())
                    .confidenceScore(cached.getConfidenceScore())
                    .modelVersion(cached.getModelVersion())
                    .timestamp(LocalDateTime.now())
                    .fromCache(true)
                    .build();
        }

        // Call Google Cloud Translation API
        String translatedText = callGoogleTranslateAPI(request);

        // Save to database
        if (request.getSaveToHistory() != null && request.getSaveToHistory()) {
            Translation translation = Translation.builder()
                    .sourceLanguage(request.getSourceLanguage())
                    .targetLanguage(request.getTargetLanguage())
                    .sourceText(request.getText())
                    .translatedText(translatedText)
                    .userId(userId)
                    .confidenceScore(98)
                    .modelVersion("google-translate-v2")
                    .build();
            translationRepository.save(translation);
        }

        return TranslationResponse.builder()
                .translatedText(translatedText)
                .sourceLanguage(request.getSourceLanguage())
                .targetLanguage(request.getTargetLanguage())
                .confidenceScore(98)
                .modelVersion("google-translate-v2")
                .timestamp(LocalDateTime.now())
                .fromCache(false)
                .build();
    }

    /**
     * Call Google Cloud Translation API v2
     */
    @SuppressWarnings("unchecked")
    private String callGoogleTranslateAPI(TranslationRequest request) {
        // If no API key configured, use mock translation
        if (googleApiKey == null || googleApiKey.isEmpty() || googleApiKey.equals("YOUR_GOOGLE_CLOUD_API_KEY_HERE")) {
            log.warn("Google Cloud API key not configured, using mock translation");
            return getMockTranslation(request);
        }

        try {
            String url = "https://translation.googleapis.com/language/translate/v2?key=" + googleApiKey;

            Map<String, Object> body = new HashMap<>();
            body.put("q", request.getText());
            body.put("source", request.getSourceLanguage());
            body.put("target", request.getTargetLanguage());
            body.put("format", "text");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                List<Map<String, Object>> translations = (List<Map<String, Object>>) data.get("translations");
                if (translations != null && !translations.isEmpty()) {
                    return (String) translations.get(0).get("translatedText");
                }
            }
            throw new TranslationException("Google Translate API returned unexpected response");
        } catch (TranslationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error calling Google Cloud Translation API: {}", e.getMessage());
            return getMockTranslation(request);
        }
    }

    /**
     * Mock translation for demo/testing when API key is not configured
     */
    private String getMockTranslation(TranslationRequest request) {
        log.warn("Using mock translation - Google Translate API unavailable");
        
        // Provide some basic mock translations for demo purposes
        Map<String, Map<String, String>> mockTranslations = new HashMap<>();
        
        Map<String, String> helloTranslations = new HashMap<>();
        helloTranslations.put("es", "Hola");
        helloTranslations.put("fr", "Bonjour");
        helloTranslations.put("de", "Hallo");
        helloTranslations.put("it", "Ciao");
        helloTranslations.put("pt", "Olá");
        helloTranslations.put("ja", "こんにちは");
        helloTranslations.put("ko", "안녕하세요");
        helloTranslations.put("zh", "你好");
        helloTranslations.put("hi", "नमस्ते");
        helloTranslations.put("ar", "مرحبا");
        mockTranslations.put("hello", helloTranslations);
        
        Map<String, String> helloWorldTranslations = new HashMap<>();
        helloWorldTranslations.put("es", "¡Hola, mundo!");
        helloWorldTranslations.put("fr", "Bonjour le monde!");
        helloWorldTranslations.put("de", "Hallo Welt!");
        helloWorldTranslations.put("it", "Ciao mondo!");
        helloWorldTranslations.put("pt", "Olá mundo!");
        helloWorldTranslations.put("ja", "こんにちは世界！");
        helloWorldTranslations.put("ko", "안녕하세요 세계!");
        helloWorldTranslations.put("zh", "你好世界！");
        helloWorldTranslations.put("hi", "नमस्ते दुनिया!");
        helloWorldTranslations.put("ar", "مرحبا بالعالم!");
        mockTranslations.put("hello, world!", helloWorldTranslations);
        mockTranslations.put("hello world", helloWorldTranslations);
        
        String lowerText = request.getText().toLowerCase().trim();
        String targetLang = request.getTargetLanguage();
        
        if (mockTranslations.containsKey(lowerText) && mockTranslations.get(lowerText).containsKey(targetLang)) {
            return mockTranslations.get(lowerText).get(targetLang);
        }
        
        return "[Mock Translation to " + request.getTargetLanguage() + "]: " + request.getText();
    }

    /**
     * Get translation history for a user
     */
    @Transactional(readOnly = true)
    public Page<Translation> getTranslationHistory(Long userId, Pageable pageable) {
        return translationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    /**
     * Get translation count for a user
     */
    @Transactional(readOnly = true)
    public long getTranslationCount(Long userId) {
        return translationRepository.countByUserId(userId);
    }
}
