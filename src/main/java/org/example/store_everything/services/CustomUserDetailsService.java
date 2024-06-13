package org.example.store_everything.services;

import org.example.store_everything.models.Role;
import org.example.store_everything.models.User;
import org.example.store_everything.repositories.RoleRepository;
import org.example.store_everything.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository,
                                    RoleRepository roleRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this).passwordEncoder(passwordEncoder);
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<User> loadAllUsers() {
        return userRepository.findAll();
    }
    public List<Role> loadAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Nie znaleziono użytkownika o loginie '" + username + "'.");
        }

        return user;
    }
    public User loadUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono użytkownika o ID '" + id + "'."));
    }

    public void setUserAuthority(String username, String authority) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Set<Role> roles = user.getAuthorities().stream().map(role -> (Role) role).collect(Collectors.toSet());
            roles.clear();
            roles.add(new Role(authority));
            user.setAuthorities(roles);
            userRepository.save(user);
        }
    }
}