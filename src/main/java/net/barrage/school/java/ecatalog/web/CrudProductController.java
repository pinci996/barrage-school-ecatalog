package net.barrage.school.java.ecatalog.web;

import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.ProductService;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/e-catalog/api/v1/crud/products")
public class CrudProductController {

    private final ProductService productService;


    public CrudProductController(
            ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> listProducts() {
        return productService.listProducts();
    }

    @PostMapping("/save")
    public void saveProducts() {
        productService.saveProducts();
    }


    @GetMapping(path = "/{productId}")
    public Optional<Product> getProductById(@PathVariable("productId") UUID productId) {
        return productService.getProductById(productId);
    }

    @DeleteMapping(path = "/{productId}")
    public void deleteProduct(@PathVariable("productId") UUID productId) {
        productService.deleteProduct(productId);
    }

    @PutMapping(path = "/{productId}")
    public void updateProduct(
            @PathVariable("productId") UUID productId,
            @RequestBody Product updatedProduct)
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String description,
//            @RequestParam(required = false) Double price) {
    {
        productService.updateProduct(productId, updatedProduct);
    }


}
