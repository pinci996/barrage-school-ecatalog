package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

}
