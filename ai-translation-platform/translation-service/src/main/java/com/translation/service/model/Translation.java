package com.translation.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a translation record
 */
@Entity
@Table(name = "translations", indexes = {
    @Index(name = "idx_source_target_lang", columnList = "sourceLanguage,targetLanguage"),
    @Index(name = "idx_user_id", columnList = "userId")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String sourceLanguage;

    @Column(nullable = false, length = 10)
    private String targetLanguage;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sourceText;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String translatedText;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private Integer confidenceScore;

    @Column(length = 50)
    private String modelVersion;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
