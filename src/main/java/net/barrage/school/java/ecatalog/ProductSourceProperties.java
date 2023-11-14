package net.barrage.school.java.ecatalog;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "ecatalog.products")
@ConfigurationPropertiesScan
public class ProductSourceProperties {

    public List<SourceRecord> sources;

    @Data
    public static class SourceRecord {
        private String name;
        private String format;
        private String url;
    }
}
