package net.barrage.school.java.ecatalog.web;

import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.MerchantService;
import net.barrage.school.java.ecatalog.model.Merchant;
import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/e-catalog/api/v1/crud/merchants")
public class CrudMerchantController {

    private final MerchantService merchantService;

    public CrudMerchantController(
            MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping
    public List<Merchant> listMerchants() {
        var merchants = merchantService.listMerchants();
        log.trace("listMerchants -> {}", merchants);
        return merchants;
    }

    @GetMapping(path = "/{merchantId}")
    public Optional<Merchant> getMerchantById(@PathVariable("merchantId") Long merchantId) {
        return merchantService.getMerchantById(merchantId);
    }

    @GetMapping(path = "/{merchantId}/products")
    public List<Product> listProductsFromMerchant(@PathVariable Long merchantId) {
        return merchantService.getProductsFromMerchant(merchantId);
    }

}
