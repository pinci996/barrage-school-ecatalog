package net.barrage.school.java.ecatalog.seeders;

import jakarta.annotation.PostConstruct;
import net.barrage.school.java.ecatalog.app.MerchantRepository;
import net.barrage.school.java.ecatalog.app.RoleRepository;
import net.barrage.school.java.ecatalog.app.UserRepository;
import net.barrage.school.java.ecatalog.model.Role;
import net.barrage.school.java.ecatalog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseSeeder {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    RoleRepository roleRepository;

    @PostConstruct
    public void seed() {
        seedRolesTable();
        seedUsersTable();
    }

    private void seedRolesTable() {
        if (!roleRepository.existsByRole("USER")) {
            Role newRole = new Role();
            newRole.setRole("USER");
            roleRepository.save(newRole);
        }
    }

    private void seedUsersTable() {
        if (!userRepository.existsByUsername("John")) {
            Role userRole = roleRepository.findByRole("USER").orElseThrow(() -> new IllegalStateException("Role does not exist"));
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            User user = new User()
                    .setPassword("$2a$12$h7hlBe0RjB4qumMcdk3AXuvTT/i/CEQDB2zGyR2E6AcQ6KvN6Uz02") // "Password"
                    .setUsername("John")
                    .setRoles(roles);
            userRepository.save(user);
        }
    }
}
