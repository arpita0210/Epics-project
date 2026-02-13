package com.translation.service.repository;

import com.translation.service.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Language entity
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    /**
     * Find language by code
     */
    Optional<Language> findByCode(String code);

    /**
     * Find all active languages
     */
    List<Language> findByActiveTrue();

    /**
     * Find languages supporting text translation
     */
    List<Language> findByActiveTrueAndSupportsTextTranslationTrue();

    /**
     * Find languages supporting speech recognition
     */
    List<Language> findByActiveTrueAndSupportsSpeechRecognitionTrue();

    /**
     * Find languages supporting speech synthesis
     */
    List<Language> findByActiveTrueAndSupportsSpeechSynthesisTrue();
}
