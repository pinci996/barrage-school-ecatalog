package net.barrage.school.java.ecatalog.web;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.MerchantService;
import net.barrage.school.java.ecatalog.app.ProductService;
import net.barrage.school.java.ecatalog.app.ProductSyncService;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/e-catalog/api/v1/products")
public class ProductController {

    private final ProductService productService;

    private final ProductSyncService productSyncService;

    private final MerchantService merchantService;


    public ProductController(
            ProductService productService,
            ProductSyncService productSyncService,
            MerchantService merchantService) {
        this.productService = productService;
        this.productSyncService = productSyncService;
        this.merchantService = merchantService;
    }

    @SneakyThrows
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/list")
    public List<Product> listProducts() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("user = {}", authentication);
        Object principal = authentication.getPrincipal();
        log.info("principal = {}", principal);
        return productService.listProducts();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam("q") String query
    ) {
        return productService.searchProducts(query);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public void createProducts(@RequestBody Product newProduct) {
        productService.createProduct(newProduct);
    }


    @GetMapping(path = "/{productId}")
    public Optional<Product> getProductById(@PathVariable("productId") UUID productId) {
        return productService.getProductById(productId);
    }

    @SneakyThrows
    @DeleteMapping(path = "/{productId}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
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

    @PostMapping(path = "/merchants/{merchantName}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public void syncSingleMerchant(@PathVariable("merchantName") String merchantName) {
        productSyncService.syncProductsForMerchant(merchantName);
    }

    @GetMapping(path = "/merchants/{merchantId}")
    public List<Product> listProductsFromMerchant(@PathVariable("merchantId") Long merchantId) {
        var products = productService.getProductsFromMerchant(merchantService.getMerchantById(merchantId));
        log.trace("listProductsByMerchantId -> {}", products);
        return products;
    }
}
