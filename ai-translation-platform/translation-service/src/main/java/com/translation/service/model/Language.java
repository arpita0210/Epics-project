package com.translation.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing supported languages
 */
@Entity
@Table(name = "languages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code; // e.g., "en", "es", "fr"

    @Column(nullable = false, length = 100)
    private String name; // e.g., "English", "Spanish", "French"

    @Column(nullable = false, length = 100)
    private String nativeName; // e.g., "English", "Español", "Français"

    @Column(nullable = false)
    private Boolean active = true;

    @Column
    private Boolean supportsTextTranslation = true;

    @Column
    private Boolean supportsSpeechRecognition = false;

    @Column
    private Boolean supportsSpeechSynthesis = false;
}
