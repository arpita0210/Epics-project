package com.translation.service.controller;

import com.translation.common.dto.ApiResponse;
import com.translation.service.dto.LanguageDTO;
import com.translation.service.dto.TranslationRequest;
import com.translation.service.dto.TranslationResponse;
import com.translation.service.model.Translation;
import com.translation.service.service.LanguageService;
import com.translation.service.service.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for translation operations
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Translation", description = "Translation API endpoints")
public class TranslationController {

    private final TranslationService translationService;
    private final LanguageService languageService;

    /**
     * Translate text between languages
     */
    @PostMapping("/translate")
    @Operation(
        summary = "Translate text",
        description = "Translate text from source language to target language using AI models"
    )
    public ResponseEntity<ApiResponse<TranslationResponse>> translate(
            @Valid @RequestBody TranslationRequest request,
            Authentication authentication) {
        
        // Extract user ID from authentication (mock for now)
        Long userId = authentication != null ? 1L : null;
        
        TranslationResponse response = translationService.translate(request, userId);
        return ResponseEntity.ok(ApiResponse.success("Translation completed successfully", response));
    }

    /**
     * Get all supported languages
     */
    @GetMapping("/languages")
    @Operation(
        summary = "Get supported languages",
        description = "Retrieve list of all supported languages"
    )
    public ResponseEntity<ApiResponse<List<LanguageDTO>>> getLanguages() {
        List<LanguageDTO> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(ApiResponse.success(languages));
    }

    /**
     * Get languages supporting text translation
     */
    @GetMapping("/languages/text-translation")
    @Operation(
        summary = "Get text translation languages",
        description = "Retrieve languages that support text translation"
    )
    public ResponseEntity<ApiResponse<List<LanguageDTO>>> getTextTranslationLanguages() {
        List<LanguageDTO> languages = languageService.getTextTranslationLanguages();
        return ResponseEntity.ok(ApiResponse.success(languages));
    }

    /**
     * Get translation history for authenticated user
     */
    @GetMapping("/translation/history")
    @Operation(
        summary = "Get translation history",
        description = "Retrieve translation history for the authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Page<Translation>>> getTranslationHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        // Extract user ID from authentication (mock for now)
        Long userId = authentication != null ? 1L : null;
        
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Authentication required"));
        }
        
        Page<Translation> history = translationService.getTranslationHistory(
                userId, 
                PageRequest.of(page, size)
        );
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    /**
     * Get translation count for authenticated user
     */
    @GetMapping("/translation/count")
    @Operation(
        summary = "Get translation count",
        description = "Get total number of translations for the authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Long>> getTranslationCount(Authentication authentication) {
        // Extract user ID from authentication (mock for now)
        Long userId = authentication != null ? 1L : null;
        
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Authentication required"));
        }
        
        long count = translationService.getTranslationCount(userId);
        return ResponseEntity.ok(ApiResponse.success("Translation count retrieved", count));
    }
}
