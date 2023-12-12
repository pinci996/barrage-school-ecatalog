package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}
