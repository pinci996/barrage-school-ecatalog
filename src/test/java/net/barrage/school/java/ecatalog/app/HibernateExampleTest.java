package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("db")
@SpringBootTest
public class HibernateExampleTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ProductService productService;

    @Test
    public void save_products_to_db() {
        var merchant = merchantRepository.save(new Merchant()
                .setName("Uncle"));
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
}

//interface ProductRepositoryTest extends CrudRepository<ProductTest, UUID> {
//    List<ProductTest> findByName(String name);
//}

//@Accessors(chain = true)
//@Data
//@Entity
//class ProductTest {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    @Column(nullable = false)
//    private String name;
//
//    private String description;
//
//    private String imageUrl;
//
//    @ManyToOne
//    @JoinColumn(name = "merchant_id")
//    private MerchantTest merchant;
//}

//interface MerchantRepositoryTest extends CrudRepository<MerchantTest, Long> {
//}

//@Data
//@Entity
//@Accessors(chain = true)
//class MerchantTest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @OneToMany(mappedBy = "merchant")
//    private Set<ProductTest> products;
//}