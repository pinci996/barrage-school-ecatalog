package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * List all available products
     */
    List<Product> listProducts();

    List<Product> searchProducts(String q);

    void saveProducts();

    Optional<Product> getProductById(Long productId);

    void deleteProduct(Long productId);

    void updateProduct(Long productId, String name, String description, Double Price);

}
