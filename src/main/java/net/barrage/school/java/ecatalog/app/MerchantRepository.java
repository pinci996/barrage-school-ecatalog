package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MerchantRepository extends CrudRepository<Merchant, Long> {
    @Query("SELECT p FROM Product p WHERE p.merchant.id = ?1")
    List<Product> findProductsByMerchantId(Long merchantId);

    Optional<Merchant> findByName(String name);
    

}
