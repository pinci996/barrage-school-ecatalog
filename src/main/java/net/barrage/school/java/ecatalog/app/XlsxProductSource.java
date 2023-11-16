package net.barrage.school.java.ecatalog.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.barrage.school.java.ecatalog.config.ProductSourceProperties;
import net.barrage.school.java.ecatalog.model.Product;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class XlsxProductSource implements ProductSource {
    private final ProductSourceProperties.SourceProperty property;

    private static final Logger log = LoggerFactory.getLogger(JsonProductSource.class);

    @RequiredArgsConstructor
    @Component
    public static class Factory implements ProductSource.Factory {
        private final ObjectMapper objectMapper;

        @Override
        public Set<String> getSupportedFormats() {
            return Set.of("xlsx");
        }

        @Override
        public ProductSource create(ProductSourceProperties.SourceProperty psp) {
            return new XlsxProductSource(psp);
        }
    }

    @Override
    public List<Product> getProducts() {
        try {
            URL url = new URL(property.getUrl());

            File tempFile = File.createTempFile("tempfile", ".xlsx");
            FileUtils.copyURLToFile(url, tempFile);

            try (FileInputStream file = new FileInputStream(tempFile);
                 Workbook workbook = new XSSFWorkbook(file)) {

                Sheet sheet = workbook.getSheetAt(0);
                List<Product> products = new ArrayList<>();

                for (Row row : sheet) {
                    String name = row.getCell(0).getStringCellValue();
                    String photoUrl = row.getCell(1).getStringCellValue();
                    double price;

                    Cell priceCell = row.getCell(4);
                    if (priceCell.getCellType() == CellType.NUMERIC) {
                        price = priceCell.getNumericCellValue();
                    } else if (priceCell.getCellType() == CellType.STRING) {
                        try {
                            price = Double.parseDouble(priceCell.getStringCellValue());
                        } catch (NumberFormatException e) {
                            log.warn("Error parsing price as a double", e);
                            price = 0.0;
                        }
                    } else {
                        log.warn("Unexpected cell type for price: {}", priceCell.getCellType());
                        price = 0.0;
                    }
                    Product product = new Product();
                    product.setName(name);
                    product.setImage(photoUrl);
                    product.setDescription(null);
                    product.setPrice(price);
                    product.setId(UUID.randomUUID());

                    products.add(product);
                }

                return products;
            } finally {
                FileUtils.deleteQuietly(tempFile);
            }
        } catch (IOException e) {
            log.warn("Oops!", e);
            throw new RuntimeException(e);
        }
    }

}
