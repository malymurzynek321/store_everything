package org.example.store_everything.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[a-z]*")
    private String username;

    @NotBlank
    @Size(min = 5)
    private String password;

    private Set<Role> roles = new HashSet<>();

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[A-Z][a-z]*")
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[A-Z][a-z]*")
    private String surname;

    @Min(18)
    private int age;

    public User() {}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }
    public void setAuthorities(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getNameAndSurname() {
        return name + " " + surname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + "'" +
                ", username='" + username + "'" +
                ", password='" + password + "'" +
                ", roles='" + roles + "'" +
                ", name='" + name + "'" +
                ", surname='" + surname + "'" +
                ", age='" + age + "'" +
                "}";
    }

    @Override public boolean isAccountNonExpired() {
        return true;
    }
    @Override public boolean isAccountNonLocked() {
        return true;
    }
    @Override public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override public boolean isEnabled() {
        return true;
    }
}