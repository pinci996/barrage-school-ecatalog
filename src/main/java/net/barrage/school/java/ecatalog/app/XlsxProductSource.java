package net.barrage.school.java.ecatalog.app;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.barrage.school.java.ecatalog.config.ProductSourceProperties;
import net.barrage.school.java.ecatalog.model.Product;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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

        @Override
        public Set<String> getSupportedFormats() {
            return Set.of("xlsx");
        }

        @Override
        public ProductSource create(ProductSourceProperties.SourceProperty psp) {
            return new XlsxProductSource(psp);
        }
    }

    @SneakyThrows
    public boolean isRemote() {
        return property.isRemote();
    }

    @SneakyThrows
    public String getName() {
        return property.getName();
    }

    @Override
    public List<Product> getProducts() {
        try {
            URL url = new URL(property.getUrl());
            InputStream inputStream = url.openStream();
            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = sheet.iterator();
            List<Product> productList = new ArrayList<>();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if (currentRow.getRowNum() == 0) {
                    continue;
                }

                Product product = new Product();
                product.setName(currentRow.getCell(0).getStringCellValue());
                product.setImage(currentRow.getCell(1).getStringCellValue());
                product.setId(UUID.randomUUID());
                product.setDescription(null);
                product.setPrice(Double.parseDouble(currentRow.getCell(4).getStringCellValue()));

                productList.add(product);
            }

            workbook.close();
            return productList;

        } catch (Exception e1) {
            log.warn("Oops!", e1);
            throw new RuntimeException(e1);
        }
    }

}
