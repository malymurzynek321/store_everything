package org.example.store_everything.services;

import org.example.store_everything.models.Category;
import org.example.store_everything.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID kategorii nie może być null-em.");
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono kategorii o ID '" + id + "'."));
    }

    public void editCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategoryById(String id) {
        categoryRepository.deleteById(id);
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }
    public List<Category> getCategoriesByOwnerId(String userId) {
        return categoryRepository.findByOwnerId(userId);
    }
}
