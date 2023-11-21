package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;

import java.util.List;
import java.util.Optional;

public interface MerchantService {
    List<Merchant> listMerchants();

    Optional<Merchant> getMerchantById(Long merchantId);

    List<Product> getProductsFromMerchant(Long merchantId);
}
