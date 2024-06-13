package org.example.store_everything.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;

@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    private String ownerId;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[a-z]*")
    private String name;

    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + "'" +
                ", ownerId='" + (ownerId != null ? ownerId : "null") + "'" +
                ", name='" + name + "'" +
                "}";
    }
}
