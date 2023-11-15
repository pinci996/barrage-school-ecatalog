package net.barrage.school.java.ecatalog.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.barrage.school.java.ecatalog.config.ProductSourceProperties;
import net.barrage.school.java.ecatalog.model.Product;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class XlsxProductSource implements ProductSource {
    private final ProductSourceProperties.SourceProperty property;
    private final ObjectMapper objectMapper;

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
            return new XlsxProductSource(psp, objectMapper);
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
                    // Assuming the columns are in order: nameA1, photo_url, quantity, id, price
                    String name = row.getCell(0).getStringCellValue();
                    String photoUrl = row.getCell(1).getStringCellValue();
                    String quantity = row.getCell(2).getStringCellValue();
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
                    SourceProduct sourceProduct = new SourceProduct();
                    sourceProduct.setName(name);
                    sourceProduct.setPhoto_url(photoUrl);
                    sourceProduct.setQuantity(quantity);
                    sourceProduct.setPrice(price);

                    Product product = convert(sourceProduct);
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




    private Product convert(XlsxProductSource.SourceProduct sourceProduct) {
        var product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(sourceProduct.getName());
        product.setDescription(sourceProduct.getQuantity());
        product.setImage(Collections.singletonList(sourceProduct.getPhoto_url()).toString());
        product.setPrice(sourceProduct.getPrice());
        return product;
    }

    static class SourceProductList extends ArrayList<SourceProduct> {
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class SourceProduct {
        private String name;
        private String photo_url;
        private String quantity;
        private double price;
    }

}
