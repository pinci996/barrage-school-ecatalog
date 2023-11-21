package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByName(String name);
}
