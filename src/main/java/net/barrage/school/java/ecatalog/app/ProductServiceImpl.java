package net.barrage.school.java.ecatalog.app;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final List<ProductSource> productSources;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ProductRepository productRepository;

    public ProductServiceImpl(
            List<ProductSource> productSources) {
        this.productSources = productSources;
    }

    @SneakyThrows
    @Override
    @Cacheable("products")
    public List<Product> listProducts() {
        return (List<Product>) productRepository.findAll();
    }

    @SneakyThrows
    @Override
    public List<Product> searchProducts(String q) {
        return productRepository.findByName(q);
    }

    @SneakyThrows
    @Override
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
        var product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException(
                "product with id" + productId + "does not exist."
        ));
        if (product.getMerchant().isRemote()) {
            throw new UnsupportedOperationException("Product cannot be modified.");
        }
        productRepository.deleteById(productId);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void updateProduct(UUID productId, Product updatedProduct) {
        var product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException(
                "product with id" + productId + "does not exist."
        ));
        if (product.getMerchant().isRemote()) {
            throw new UnsupportedOperationException("Product cannot be modified.");
        }
        product.setName(updatedProduct.getName())
                .setDescription(updatedProduct.getDescription())
                .setPrice(updatedProduct.getPrice());
        productRepository.save(product);
    }

    @Scheduled(fixedRate = 100000)
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() {
    }

    @Scheduled(fixedRate = 100000)
    @CacheEvict(value = "search", allEntries = true)
    public void clearSearchCache() {
    }


}
