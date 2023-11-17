package net.barrage.school.java.ecatalog.config;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class SchedulerConfiguration {
    @Scheduled(fixedRate = 5000)
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() {
    }

    @Scheduled(fixedRate = 10000)
    @CacheEvict(value = "search", allEntries = true)
    public void clearSearchCache() {
    }
}
