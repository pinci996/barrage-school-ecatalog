package net.barrage.school.java.ecatalog.web;

import lombok.extern.slf4j.Slf4j;
import net.barrage.school.java.ecatalog.app.MerchantService;
import net.barrage.school.java.ecatalog.model.Merchant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/e-catalog/api/v1/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(
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
    public Merchant getMerchantById(@PathVariable("merchantId") Long merchantId) {
        return merchantService.getMerchantById(merchantId);
    }
}
