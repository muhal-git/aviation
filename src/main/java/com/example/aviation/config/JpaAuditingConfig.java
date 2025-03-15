package com.example.aviation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.domain.AuditorAware;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaAuditingConfig {
    
    private final DatabaseProperties databaseProperties;
    
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(databaseProperties.username());
    }
}