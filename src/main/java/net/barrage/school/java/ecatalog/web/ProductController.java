package net.barrage.school.java.ecatalog.web;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.ProductService;
import net.barrage.school.java.ecatalog.app.ProductSyncService;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/e-catalog/api/v1/products")
public class ProductController {

    private final ProductService productService;

    private final ProductSyncService productSyncService;


    public ProductController(
            ProductService productService,
            ProductSyncService productSyncService) {
        this.productService = productService;
        this.productSyncService = productSyncService;
    }

    @GetMapping("/list")
    public List<Product> listProducts() {
        return productService.listProducts();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam("q") String query
    ) {
        return productService.searchProducts(query);
    }

    @PostMapping("/save")
    public void saveProducts() {
        productService.saveProducts();
    }

    @PostMapping
    public void createProducts(@RequestBody Product newProduct) {
        productService.createProduct(newProduct);
    }


    @GetMapping(path = "/{productId}")
    public Optional<Product> getProductById(@PathVariable("productId") UUID productId) {
        return productService.getProductById(productId);
    }

    @SneakyThrows
    @DeleteMapping(path = "/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") UUID productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (UnsupportedOperationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping(path = "/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable("productId") UUID productId,
            @RequestBody Product updatedProduct) {
        try {
            productService.updateProduct(productId, updatedProduct);
            return ResponseEntity.ok("Product updated successfully");
        } catch (UnsupportedOperationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping(path = "/{merchantName}")
    public void syncSingleMerchant(@PathVariable("merchantName") String merchantName) {
        productSyncService.syncProductsForMerchant(merchantName);
    }

    @GetMapping(path = "/{merchantId}")
    public Set<Product> listProductsFromMerchant(@PathVariable Long merchantId) {
        return productService.getProductsFromMerchant(merchantId);
    }
}
