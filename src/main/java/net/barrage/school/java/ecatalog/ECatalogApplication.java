package net.barrage.school.java.ecatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConfigurationPropertiesScan
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class ECatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECatalogApplication.class, args);
    }

}