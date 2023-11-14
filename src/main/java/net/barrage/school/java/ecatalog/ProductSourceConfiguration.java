package net.barrage.school.java.ecatalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import net.barrage.school.java.ecatalog.app.ProductSource;

import java.util.List;

import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductSourceConfiguration {
    private final ProductSourceProperties properties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public ProductSourceConfiguration(ProductSourceProperties properties, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Bean
    public List<ProductSource> jsonProductSources() {
        return properties.getSources().stream()
                .filter(sourceRecord -> "json".equalsIgnoreCase(sourceRecord.getFormat()))
                .map(this::createJsonProductSource)
                .toList();
    }

    @Bean
    public List<ProductSource> xmlProductSources() {
        return properties.getSources().stream()
                .filter(sourceRecord -> "xml".equalsIgnoreCase(sourceRecord.getFormat()))
                .map(this::createXmlProductSource)
                .toList();
    }

    @Bean
    public List<ProductSource> xlsxProductSources() {
        return properties.getSources().stream()
                .filter(sourceRecord -> "xlsx".equalsIgnoreCase(sourceRecord.getFormat()))
                .map(this::createXlsxProductSource)
                .toList();
    }

    private ProductSource createJsonProductSource(ProductSourceProperties.SourceRecord sourceRecord) {
        return new ProductSource() {
            @Override
            public List<Product> getProducts(String sourceFormat) {
                if ("json".equalsIgnoreCase(sourceRecord.getFormat())) {
                    String jsonData = restTemplate.getForObject(sourceRecord.getUrl(), String.class);
                    return convertJsonToProducts(jsonData);
                }
                return List.of();
            }
        };
    }

    private ProductSource createXmlProductSource(ProductSourceProperties.SourceRecord sourceRecord) {
        return new ProductSource() {
            @Override
            public List<Product> getProducts(String sourceFormat) {
                if ("xml".equalsIgnoreCase(sourceRecord.getFormat())) {
                    // Logic to handle XML source
                    return List.of();
                }
                return List.of();
            }
        };
    }

    private ProductSource createXlsxProductSource(ProductSourceProperties.SourceRecord sourceRecord) {
        return new ProductSource() {
            @Override
            public List<Product> getProducts(String sourceFormat) {
                if ("xlsx".equalsIgnoreCase(sourceRecord.getFormat())) {
                    // Logic to handle XLSX source
                    return List.of();
                }
                return List.of();
            }
        };
    }

    private List<Product> convertJsonToProducts(String jsonData) {
        // Logic to convert json
        try {
            return objectMapper.readValue(jsonData, new TypeReference<List<Product>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Product> convertXmlToProducts(String xmlData) {
        // Logic to convert xml
        try {
            return objectMapper.readValue(xmlData, new TypeReference<List<Product>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Product> convertXlsxToProducts(String xlsxData) {
        // Logic to convert xlsx
        try {
            return objectMapper.readValue(xlsxData, new TypeReference<List<Product>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
