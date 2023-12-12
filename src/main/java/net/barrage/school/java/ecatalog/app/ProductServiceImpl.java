package net.barrage.school.java.ecatalog.app;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(
            ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SneakyThrows
    @Override
    @Cacheable("products")
    public List<Product> listProducts() {
        List<Product> productList = new ArrayList<>();
        productRepository.findAll().forEach(productList::add);
        return productList;
    }

    @SneakyThrows
    @Override
    public List<Product> searchProducts(String q) {
        return productRepository.findByName(q);
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

    public void createProduct(Product newProduct) {
        var product = new Product()
                .setMerchant(newProduct.getMerchant())
                .setName(newProduct.getName())
                .setDescription(newProduct.getDescription())
                .setImage(newProduct.getImage())
                .setPrice(newProduct.getPrice());

        productRepository.save(product);
    }

    @SneakyThrows
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsFromMerchant(Merchant merchant) {
        return productRepository.findByMerchant(merchant);
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
