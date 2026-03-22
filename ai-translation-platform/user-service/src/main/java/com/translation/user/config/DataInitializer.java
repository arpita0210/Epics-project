package com.translation.user.config;

import com.translation.user.model.Role;
import com.translation.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the database with default roles on startup
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            log.info("Seeding default roles...");
            roleRepository.save(Role.builder().name(Role.RoleName.ROLE_USER).build());
            roleRepository.save(Role.builder().name(Role.RoleName.ROLE_PREMIUM).build());
            roleRepository.save(Role.builder().name(Role.RoleName.ROLE_ADMIN).build());
            log.info("Default roles seeded");
        }
    }
}
