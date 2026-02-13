package com.translation.service.service;

import com.translation.service.dto.TranslationRequest;
import com.translation.service.dto.TranslationResponse;
import com.translation.service.model.Translation;
import com.translation.service.repository.TranslationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TranslationService
 */
@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

    @Mock
    private TranslationRepository translationRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TranslationService translationService;

    private TranslationRequest request;

    @BeforeEach
    void setUp() {
        request = TranslationRequest.builder()
                .text("Hello, world!")
                .sourceLanguage("en")
                .targetLanguage("es")
                .saveToHistory(true)
                .build();
    }

    @Test
    void testTranslate_Success() {
        // Arrange
        when(translationRepository.findRecentTranslation(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(translationRepository.save(any(Translation.class)))
                .thenReturn(new Translation());

        // Act
        TranslationResponse response = translationService.translate(request, 1L);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getTranslatedText());
        assertEquals("en", response.getSourceLanguage());
        assertEquals("es", response.getTargetLanguage());
        assertFalse(response.getFromCache());
        verify(translationRepository, times(1)).save(any(Translation.class));
    }

    @Test
    void testTranslate_FromCache() {
        // Arrange
        Translation cachedTranslation = Translation.builder()
                .sourceText("Hello, world!")
                .translatedText("¡Hola, mundo!")
                .sourceLanguage("en")
                .targetLanguage("es")
                .confidenceScore(95)
                .modelVersion("v1.0")
                .build();

        when(translationRepository.findRecentTranslation(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(cachedTranslation));

        // Act
        TranslationResponse response = translationService.translate(request, 1L);

        // Assert
        assertNotNull(response);
        assertEquals("¡Hola, mundo!", response.getTranslatedText());
        assertTrue(response.getFromCache());
        verify(translationRepository, never()).save(any(Translation.class));
    }

    @Test
    void testGetTranslationCount() {
        // Arrange
        when(translationRepository.countByUserId(1L)).thenReturn(10L);

        // Act
        long count = translationService.getTranslationCount(1L);

        // Assert
        assertEquals(10L, count);
        verify(translationRepository, times(1)).countByUserId(1L);
    }
}
