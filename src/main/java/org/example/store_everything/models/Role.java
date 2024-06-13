package org.example.store_everything.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import jakarta.validation.constraints.*;

@Document(collection = "roles")
public class Role implements GrantedAuthority {

    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[A-Z]*")
    private String name;

    public Role(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return name;
    }
    public void setAuthority(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + "'" +
                ", name='" + name + "'" +
                "}";
    }
}