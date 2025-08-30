package com.example.bankcards.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Включает планировщик задач Spring для компонентов с аннотацией @Scheduled.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
} 