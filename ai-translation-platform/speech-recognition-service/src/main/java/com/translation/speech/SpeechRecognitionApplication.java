package com.translation.speech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.translation.speech", "com.translation.common"})
public class SpeechRecognitionApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpeechRecognitionApplication.class, args);
    }
}
