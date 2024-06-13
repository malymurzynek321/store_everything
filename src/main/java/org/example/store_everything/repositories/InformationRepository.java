package org.example.store_everything.repositories;

import org.example.store_everything.models.Information;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InformationRepository extends MongoRepository<Information, String> {
    List<Information> findByOwnerId(String ownerId);
    Optional<Information> findByUrl(String url);
    long countByCategoryId(String categoryId);
}
