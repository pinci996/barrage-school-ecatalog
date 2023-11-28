package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
    List<Product> findByName(String name);

    List<Product> findByMerchant(Merchant merchant);

    void deleteByMerchantId(Long merchantId);
}
