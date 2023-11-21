package net.barrage.school.java.ecatalog.web;

import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.MerchantService;
import net.barrage.school.java.ecatalog.app.ProductService;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/e-catalog/api/v1")
public class CrudProductController {

    private final ProductService productService;

    private final MerchantService merchantService;

    public CrudProductController(
            ProductService productService, MerchantService merchantService) {
        this.productService = productService;
        this.merchantService = merchantService;
    }

    @GetMapping
    public List<Product> listProducts() {
        return productService.listProducts();
    }

    @PostMapping("/products/save")
    public void saveProducts() {
        productService.saveProducts();
    }

    @GetMapping("/merchants")
    public List<Merchant> listMerchants() {
        var merchants = merchantService.listMerchants();
        log.trace("listMerchants -> {}", merchants);
        return merchants;
    }

    @GetMapping(path = "/merchants/{merchantId}")
    public Optional<Merchant> getMerchantById(@PathVariable("merchantId") Long merchantId) {
        return merchantService.getMerchantById(merchantId);
    }

    @GetMapping(path = "/products/{productId}")
    public Optional<Product> getProductById(@PathVariable("productId") Long productId) {
        return productService.getProductById(productId);
    }

    @DeleteMapping(path = "/products/{productId}")
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
    }

    @PutMapping(path = "/products/{productId}")
    public void updateProduct(
            @PathVariable("productId") Long productId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double price) {
        productService.updateProduct(productId, name, description, price);
    }

    @GetMapping(path = "/merchants/{merchantId}/products")
    public List<Product> listProductsFromMerchant(@PathVariable Long merchantId) {
        return merchantService.getProductsFromMerchant(merchantId);
    }

}
