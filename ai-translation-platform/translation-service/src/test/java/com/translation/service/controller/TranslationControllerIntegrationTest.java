package com.translation.service.controller;

import com.translation.service.dto.TranslationRequest;
import com.translation.service.dto.TranslationResponse;
import com.translation.service.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for TranslationController
 */
@SpringBootTest
@AutoConfigureMockMvc
class TranslationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    @Test
    void testTranslateEndpoint_Success() throws Exception {
        // Arrange
        TranslationResponse mockResponse = TranslationResponse.builder()
                .translatedText("¡Hola, mundo!")
                .sourceLanguage("en")
                .targetLanguage("es")
                .confidenceScore(95)
                .modelVersion("v1.0")
                .timestamp(LocalDateTime.now())
                .fromCache(false)
                .build();

        when(translationService.translate(any(TranslationRequest.class), anyLong()))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "text": "Hello, world!",
                        "sourceLanguage": "en",
                        "targetLanguage": "es"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.translatedText").value("¡Hola, mundo!"))
                .andExpect(jsonPath("$.data.sourceLanguage").value("en"))
                .andExpect(jsonPath("$.data.targetLanguage").value("es"));
    }

    @Test
    void testTranslateEndpoint_ValidationError() throws Exception {
        // Act & Assert - Missing required fields
        mockMvc.perform(post("/api/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "text": ""
                    }
                    """))
                .andExpect(status().isBadRequest());
    }
}
