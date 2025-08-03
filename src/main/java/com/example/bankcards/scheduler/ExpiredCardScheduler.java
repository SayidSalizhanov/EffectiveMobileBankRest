package com.example.bankcards.scheduler;

import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "card.expired.scheduler.enabled", havingValue = "true")
public class ExpiredCardScheduler {

    private final CardService cardService;

    @Scheduled(cron = "0 0 2 1 * ?") // every month 02:00
    public void processExpiredCards() {
        try {
            log.info("Starting expired cards processing for current month...");
            
            cardService.updateExpiredCards();
            
            log.info("Expired cards processing completed successfully");
        } catch (Exception e) {
            log.error("Error during expired cards processing: {}", e.getMessage(), e);
        }
    }
} 