package net.barrage.school.java.ecatalog.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.config.ProductSourceProperties;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

//@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    // FYI - https://www.baeldung.com/jackson-object-mapper-tutorial
    // AR: You dont need it anymore
    private final ObjectMapper objectMapper;
    private final List<ProductSource> productSources;

    // to delete
    @Autowired
    private ProductSourceProperties properties;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ProductRepository productRepository;

    public ProductServiceImpl(
            ObjectMapper objectMapper,
            List<ProductSource> productSources) {
        this.objectMapper = objectMapper;
        this.productSources = productSources;
    }

    @SneakyThrows
    @Override
    @Cacheable("products")
    public List<Product> listProducts() {
        // AR: Why do you continue read products from sources here? Shouldn't we get them from db?
        var result = new ArrayList<Product>();
        for (var ps : productSources) {
            result.addAll(ps.getProducts());
        }
        return result;
    }

    @SneakyThrows
    @Override
    // AR: It can't work like that, explain me why!
    @Cacheable("search")
    public List<Product> searchProducts(String q) {
        var result = new ArrayList<Product>();
        for (var ps : productSources) {
            result.addAll(ps.getProducts());
        }
        return result.stream()
                .filter(p -> Objects.requireNonNullElse(p.getName(), "").toLowerCase().contains(q) || Objects.requireNonNullElse(p.getDescription(), "").toLowerCase().contains(q))
                .toList();
    }

    @SneakyThrows
    @Override
    // AR: That's bad idea. For whom remote=true u have to keep syncing automatically by cron.
    // For whom remote=false u need to do it only once explicitly.
    // U cant fit to ur goals with this API. U'd better to have explicit REST co u can choose who gonna be synced.
    public void saveProducts() {
        for (var ps : productSources) {
            if (ps.isRemote()) {
                continue;
            }
            var name = ps.getName();

            var merchant = merchantRepository.save(new Merchant()
                    .setName(name));

            var allProducts = ps.getProducts().stream()
                    .map(p -> new Product()
                            .setMerchant(merchant)
                            .setId(p.getId())
                            .setName(p.getName())
                            .setDescription(p.getDescription())
                            .setPrice((p.getPrice()))
                            .setImage(p.getImage()))
                    .toList();
            productRepository.saveAll(allProducts);
        }
    }

    @SneakyThrows
    @Override
    public Optional<Product> getProductById(UUID productId) {
        return productRepository.findById(productId);
    }

    @SneakyThrows
    @Override
    public void deleteProduct(UUID productId) {
        boolean exists = productRepository.existsById(productId);
        if (!exists) {
            throw new IllegalStateException("product with id" + productId + "does not exist");
        }
        productRepository.deleteById(productId);

    }

    @SneakyThrows
    @Override
    @Transactional
    public void updateProduct(UUID productId, Product updatedProduct) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException(
                "product with id" + productId + "does not exist."
        ));

        // AR: Don't think u really need these check + u can do similar by calling just
        //  productRepository.save(updatedProduct);

        if (updatedProduct.getName() != null && !updatedProduct.getName().isEmpty() &&
                !Objects.equals(product.getName(), updatedProduct.getName())) {
            product.setName(updatedProduct.getName());
        }

        if (updatedProduct.getDescription() != null && !updatedProduct.getDescription().isEmpty() &&
                !Objects.equals(product.getDescription(), updatedProduct.getDescription())) {
            product.setDescription(updatedProduct.getDescription());
        }

        if (updatedProduct.getPrice() != null && !Objects.equals(product.getPrice(), updatedProduct.getPrice())) {
            product.setPrice(updatedProduct.getPrice());
        }
    }


    // AR: 5 sec cache? Really?
    @Scheduled(fixedRate = 5000)
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() {
    }

    @Scheduled(fixedRate = 10000)
    @CacheEvict(value = "search", allEntries = true)
    public void clearSearchCache() {
    }


}
