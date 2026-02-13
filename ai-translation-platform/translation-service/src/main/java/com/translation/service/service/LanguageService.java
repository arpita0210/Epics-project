package com.translation.service.service;

import com.translation.service.dto.LanguageDTO;
import com.translation.service.model.Language;
import com.translation.service.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing supported languages
 */
@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    /**
     * Get all active languages
     */
    @Transactional(readOnly = true)
    public List<LanguageDTO> getAllLanguages() {
        return languageRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get languages supporting text translation
     */
    @Transactional(readOnly = true)
    public List<LanguageDTO> getTextTranslationLanguages() {
        return languageRepository.findByActiveTrueAndSupportsTextTranslationTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get languages supporting speech recognition
     */
    @Transactional(readOnly = true)
    public List<LanguageDTO> getSpeechRecognitionLanguages() {
        return languageRepository.findByActiveTrueAndSupportsSpeechRecognitionTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get languages supporting speech synthesis
     */
    @Transactional(readOnly = true)
    public List<LanguageDTO> getSpeechSynthesisLanguages() {
        return languageRepository.findByActiveTrueAndSupportsSpeechSynthesisTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Language entity to DTO
     */
    private LanguageDTO convertToDTO(Language language) {
        return LanguageDTO.builder()
                .code(language.getCode())
                .name(language.getName())
                .nativeName(language.getNativeName())
                .supportsTextTranslation(language.getSupportsTextTranslation())
                .supportsSpeechRecognition(language.getSupportsSpeechRecognition())
                .supportsSpeechSynthesis(language.getSupportsSpeechSynthesis())
                .build();
    }
}
