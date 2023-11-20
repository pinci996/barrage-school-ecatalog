package net.barrage.school.java.ecatalog.web;

import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/e-catalog/api/v1/products")
public class DbController {

    private final ProductService productService;

    public DbController(
            ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public void saveProducts() {
        productService.saveProducts();
    }
}
