package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Product;

import java.util.List;

public interface ProductSource {
    List<Product> getProducts(String sourceFormat);

}
