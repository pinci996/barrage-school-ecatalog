package net.barrage.school.java.ecatalog.app;

import net.barrage.school.java.ecatalog.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends CrudRepository<Role, UUID> {

    Optional<Role> findByRole(String role);

    boolean existsByRole(String role);
}
