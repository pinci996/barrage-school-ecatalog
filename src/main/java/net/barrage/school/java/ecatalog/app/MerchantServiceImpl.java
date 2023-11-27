package net.barrage.school.java.ecatalog.app;

import lombok.SneakyThrows;
import net.barrage.school.java.ecatalog.model.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@RequiredArgsConstructor
@Service
public class MerchantServiceImpl implements MerchantService {


    @Autowired
    MerchantRepository merchantRepository;

    public MerchantServiceImpl(
            MerchantRepository merchantRepository
    ) {
        this.merchantRepository = merchantRepository;
    }


    @SneakyThrows
    @Override
    public List<Merchant> listMerchants() {
        return (List<Merchant>) merchantRepository.findAll();
    }

    @SneakyThrows
    @Override
    public Optional<Merchant> getMerchantById(Long merchantId) {
        return merchantRepository.findById(merchantId);
    }
}
