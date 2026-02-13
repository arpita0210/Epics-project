package com.translation.service.repository;

import com.translation.service.model.Translation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Translation entity
 */
@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    /**
     * Find translation history for a user
     */
    Page<Translation> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find exact translation match for caching
     */
    @Query("SELECT t FROM Translation t WHERE " +
           "t.sourceText = :sourceText AND " +
           "t.sourceLanguage = :sourceLang AND " +
           "t.targetLanguage = :targetLang " +
           "ORDER BY t.createdAt DESC")
    Optional<Translation> findRecentTranslation(
            @Param("sourceText") String sourceText,
            @Param("sourceLang") String sourceLang,
            @Param("targetLang") String targetLang
    );

    /**
     * Count translations by user
     */
    long countByUserId(Long userId);
}
