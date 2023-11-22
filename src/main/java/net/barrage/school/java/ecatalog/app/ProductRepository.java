package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

interface ProductRepository extends CrudRepository<Product, UUID> {
    List<Product> findByName(String name);

}
