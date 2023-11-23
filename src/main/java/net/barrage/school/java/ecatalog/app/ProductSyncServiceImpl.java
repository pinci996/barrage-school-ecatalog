package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSyncServiceImpl implements ProductSyncService {
    private final List<ProductSource> productSources;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ProductRepository productRepository;

    public ProductSyncServiceImpl(
            List<ProductSource> productSources) {
        this.productSources = productSources;
    }

    @Override
    @Scheduled(fixedRate = 100000)
    @Transactional
    public void syncRemoteProducts() {
        for (var ps : productSources) {
            if (!ps.isRemote()) {
                continue;
            }
            var name = ps.getName();

            Optional<Merchant> existingMerchantOptional = merchantRepository.findByName(name);

            Merchant merchant;
            if (existingMerchantOptional.isPresent()) {
                merchant = existingMerchantOptional.get();
                productRepository.deleteByMerchantId(merchant.getId());
            } else {
                merchant = merchantRepository.save(new Merchant().setName(name).setRemote(true));
            }

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
}
