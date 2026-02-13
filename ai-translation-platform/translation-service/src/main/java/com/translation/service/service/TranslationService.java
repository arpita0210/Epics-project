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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for handling translation operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final RestTemplate restTemplate;

    @Value("${ml.translation.endpoint:http://localhost:8000/translate}")
    private String mlTranslationEndpoint;

    @Value("${ml.translation.model-version:v1.0}")
    private String modelVersion;

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

        // Call ML service for translation
        String translatedText = callMLTranslationService(request);

        // Save to database if requested
        if (request.getSaveToHistory()) {
            Translation translation = Translation.builder()
                    .sourceLanguage(request.getSourceLanguage())
                    .targetLanguage(request.getTargetLanguage())
                    .sourceText(request.getText())
                    .translatedText(translatedText)
                    .userId(userId)
                    .confidenceScore(95) // Mock confidence score
                    .modelVersion(modelVersion)
                    .build();
            translationRepository.save(translation);
        }

        return TranslationResponse.builder()
                .translatedText(translatedText)
                .sourceLanguage(request.getSourceLanguage())
                .targetLanguage(request.getTargetLanguage())
                .confidenceScore(95)
                .modelVersion(modelVersion)
                .timestamp(LocalDateTime.now())
                .fromCache(false)
                .build();
    }

    /**
     * Call external ML service for translation
     */
    private String callMLTranslationService(TranslationRequest request) {
        try {
            // Prepare request for ML service
            Map<String, Object> mlRequest = new HashMap<>();
            mlRequest.put("text", request.getText());
            mlRequest.put("source_language", request.getSourceLanguage());
            mlRequest.put("target_language", request.getTargetLanguage());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(mlRequest, headers);

            // Call ML service
            ResponseEntity<Map> response = restTemplate.exchange(
                    mlTranslationEndpoint,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("translated_text");
            } else {
                throw new TranslationException("ML service returned unexpected response");
            }
        } catch (Exception e) {
            log.error("Error calling ML translation service", e);
            // Fallback to mock translation for demo purposes
            return getMockTranslation(request);
        }
    }

    /**
     * Mock translation for demo/testing when ML service is unavailable
     */
    private String getMockTranslation(TranslationRequest request) {
        log.warn("Using mock translation - ML service unavailable");
        return "[TRANSLATED from " + request.getSourceLanguage() + " to " + 
               request.getTargetLanguage() + "]: " + request.getText();
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
