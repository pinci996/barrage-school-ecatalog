package net.barrage.school.java.ecatalog.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    // FYI - https://www.baeldung.com/jackson-object-mapper-tutorial
    private final ObjectMapper objectMapper;
    private final List<ProductSource> productSources;

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
        var result = new ArrayList<Product>();
        for (var ps : productSources) {
            result.addAll(ps.getProducts());
        }
        return result;
    }

    @SneakyThrows
    @Override
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


    @Scheduled(fixedRate = 5000)
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() {
    }

    @Scheduled(fixedRate = 10000)
    @CacheEvict(value = "search", allEntries = true)
    public void clearSearchCache() {
    }


}
