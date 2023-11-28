package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    /**
     * List all available products
     */
    List<Product> listProducts();

    List<Product> searchProducts(String q);

    Optional<Product> getProductById(UUID productId);

    void createProduct(Product newProduct);

    void deleteProduct(UUID productId);

    void updateProduct(UUID productId, Product updatedProduct);

    List<Product> getProductsFromMerchant(Merchant merchant);


}
