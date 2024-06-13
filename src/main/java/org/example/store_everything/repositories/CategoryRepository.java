package org.example.store_everything.repositories;

import org.example.store_everything.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByOwnerId(String ownerId);
}
