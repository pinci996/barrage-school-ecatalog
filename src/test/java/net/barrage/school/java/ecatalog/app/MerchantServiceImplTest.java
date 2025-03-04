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

@ActiveProfiles("db")
@SpringBootTest
public class MerchantServiceImplTest {

    @Autowired
    MerchantServiceImpl impl;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ProductService productService;

    @Test
    @Transactional
    public void save_products_to_db() {
        var merchant = merchantRepository.save(new Merchant()
                .setName("Mirko"));

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
    void merchants_are_not_empty() {
        assertFalse(impl.listMerchants().isEmpty(), "Expect listMerchants() to return something.");
    }

    @Test
    @Transactional
    void get_product_by_id_should_succeed() {
        this.save_products_to_db();
        var merchants = merchantRepository.findAll();
        var firstMerchant = merchants.iterator().next();
        var resultMerchant = impl.getMerchantById(firstMerchant.getId());
        assertEquals(resultMerchant, firstMerchant);
    }
}
