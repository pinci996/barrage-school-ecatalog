package net.barrage.school.java.ecatalog.app;

import lombok.SneakyThrows;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductSyncServiceImpl implements ProductSyncService {
    private final List<ProductSource> productSources;

    private final MerchantRepository merchantRepository;

    private final ProductRepository productRepository;

    public ProductSyncServiceImpl(
            List<ProductSource> productSources,
            MerchantRepository merchantRepository,
            ProductRepository productRepository) {
        this.productSources = productSources;
        this.merchantRepository = merchantRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Scheduled(fixedRate = 10000)
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

    @Override
    @SneakyThrows
    @Transactional
    public void syncProductsForMerchant(String name) {
        for (var ps : productSources) {
            if (!Objects.equals(name, ps.getName())) {
                continue;
            }

            Merchant merchant = merchantRepository.save(new Merchant()
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
}
