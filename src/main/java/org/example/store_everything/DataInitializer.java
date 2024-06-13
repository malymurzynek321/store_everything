package org.example.store_everything;

import org.example.store_everything.models.Role;
import org.example.store_everything.models.User;
import org.example.store_everything.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));

            Set<Role> roles = new HashSet<>();
            roles.add(new Role("ADMIN"));
            admin.setAuthorities(roles);

            userRepository.save(admin);
            System.out.println("Utworzono konto administratora: admin/admin");
        }
    }
}
