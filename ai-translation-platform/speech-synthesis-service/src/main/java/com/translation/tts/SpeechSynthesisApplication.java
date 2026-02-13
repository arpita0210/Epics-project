package com.translation.tts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.translation.tts", "com.translation.common"})
public class SpeechSynthesisApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpeechSynthesisApplication.class, args);
    }
}
