package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("db")
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    ProductServiceImpl impl;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ProductService productService;

    @Test
    public void save_products_to_db() {
        var merchant = merchantRepository.save(new Merchant()
                .setName("uncle"));
        var allProducts = productService.listProducts().stream()
                .map(p -> new Product()
                        .setMerchant(merchant)
                        .setId(p.getId())
                        .setName(p.getName())
                        .setDescription(p.getDescription())
                        .setPrice(p.getPrice())
                        .setImage(p.getImage()))
                .toList();
        productRepository.saveAll(allProducts);
    }

    @Test
    void products_are_not_empty() {
        assertFalse(impl.listProducts().isEmpty(), "Expect listProducts() to return something.");
    }

    @Test
    void search_products_are_not_empty() {
        assertFalse(impl.searchProducts("Chontaleno").isEmpty(), "Expect searchProducts() to return something.");
    }

    @Test
    void search_products_should_be_empty() {
        assertTrue(impl.searchProducts("mirko").isEmpty(), "Expect searchProducts() to be empty");
    }

    @Test
    @Transactional
    void delete_product_should_succeed() {
        this.save_products_to_db();
        var products = productRepository.findAll();
        var firstProduct = products.iterator().next();
        impl.deleteProduct(firstProduct.getId());
        assertFalse(productRepository.existsById(firstProduct.getId()));
    }

    @Test
    @Transactional
    void get_product_by_id_should_succeed() {
        this.save_products_to_db();
        var products = productRepository.findAll();
        var firstProduct = products.iterator().next();
        var resultProductOptional = impl.getProductById(firstProduct.getId());
        assertTrue(resultProductOptional.isPresent());
        var resultProduct = resultProductOptional.get();
        assertEquals(resultProduct, firstProduct);
    }
}