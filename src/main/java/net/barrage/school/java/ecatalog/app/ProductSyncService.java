package net.barrage.school.java.ecatalog.app;

public interface ProductSyncService {
    void syncRemoteProducts();

    void syncProductsForMerchant(String name);
}
