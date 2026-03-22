package com.translation.service.config;

import com.translation.service.model.Language;
import com.translation.service.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Seeds the database with supported languages on startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final LanguageRepository languageRepository;

    @Override
    public void run(String... args) {
        if (languageRepository.count() == 0) {
            log.info("Seeding supported languages...");
            List<Language> languages = Arrays.asList(
                Language.builder().code("en").name("English").nativeName("English")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("es").name("Spanish").nativeName("Español")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("fr").name("French").nativeName("Français")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("de").name("German").nativeName("Deutsch")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("it").name("Italian").nativeName("Italiano")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("pt").name("Portuguese").nativeName("Português")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("ja").name("Japanese").nativeName("日本語")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("ko").name("Korean").nativeName("한국어")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("zh").name("Chinese").nativeName("中文")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("hi").name("Hindi").nativeName("हिन्दी")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("ar").name("Arabic").nativeName("العربية")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("ru").name("Russian").nativeName("Русский")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(true).supportsSpeechSynthesis(true).build(),
                Language.builder().code("nl").name("Dutch").nativeName("Nederlands")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(false).supportsSpeechSynthesis(false).build(),
                Language.builder().code("sv").name("Swedish").nativeName("Svenska")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(false).supportsSpeechSynthesis(false).build(),
                Language.builder().code("tr").name("Turkish").nativeName("Türkçe")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(false).supportsSpeechSynthesis(false).build(),
                Language.builder().code("pl").name("Polish").nativeName("Polski")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(false).supportsSpeechSynthesis(false).build(),
                Language.builder().code("th").name("Thai").nativeName("ไทย")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(false).supportsSpeechSynthesis(false).build(),
                Language.builder().code("vi").name("Vietnamese").nativeName("Tiếng Việt")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(false).supportsSpeechSynthesis(false).build(),
                Language.builder().code("bn").name("Bengali").nativeName("বাংলা")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(false).supportsSpeechSynthesis(false).build(),
                Language.builder().code("ta").name("Tamil").nativeName("தமிழ்")
                    .active(true).supportsTextTranslation(true).supportsSpeechRecognition(false).supportsSpeechSynthesis(false).build()
            );
            languageRepository.saveAll(languages);
            log.info("Seeded {} languages", languages.size());
        }
    }
}
