package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("json")
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    ProductServiceImpl impl;

    @Test
    void products_are_not_empty() {
        assertFalse(impl.listProducts().isEmpty(), "Expect listProducts() to return something.");
    }

    @Test
    void products_has_valid_fields() {
        List<String> expectedKeys = List.of("id", "name", "description", "image", "price");

        var products = impl.listProducts();

        for (Product product : products) {
            assertTrue(product.getKeys().containsAll(expectedKeys),
                    "Object does not have all the expected keys: " + product);
        }
    }

    @Test
    void search_products_are_not_empty() {
        assertFalse(impl.searchProducts("valla").isEmpty(), "Expect searchProducts() to return something.");
    }

    @Test
    void search_products_should_be_empty() {
        assertTrue(impl.searchProducts("mirko").isEmpty(), "Expect searchProducts() to be empty");
    }
}