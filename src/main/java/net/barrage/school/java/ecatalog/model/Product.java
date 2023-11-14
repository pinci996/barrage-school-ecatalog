package net.barrage.school.java.ecatalog.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Data
@ToString
@Accessors(chain=true)
public class Product {
    private UUID id;
    private String name;
    private String description;
    private String image;
    private double price;
}
