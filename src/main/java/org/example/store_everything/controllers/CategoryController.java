package org.example.store_everything.controllers;

import jakarta.validation.Valid;
import org.example.store_everything.models.Category;
import org.example.store_everything.models.User;
import org.example.store_everything.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories/create")
    public String showCategoryCreationForm(Model model) {
        model.addAttribute("category", new Category());
        return "/categories/create";
    }

    @PostMapping("/categories/create")
    public String createCategory(@Valid @ModelAttribute Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "/categories/create";
        }

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        category.setOwnerId(principal.getId());

        categoryService.createCategory(category);
        return "redirect:/entries";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategoryById(@PathVariable("id") String id, Model model) {
        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nie znaleziono kategorii o ID '" + id + "'."
            );
        }

        model.addAttribute("category", category);
        model.addAttribute("categoryId", id);

        return "/categories/edit";
    }

    @GetMapping("/categories/edit")
    public String editCategoryRedirect() {
        return "redirect:/entries";
    }

    @PostMapping("/categories/edit")
    public String showCategoryEditingForm(@Valid @ModelAttribute Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "/categories/edit";
        }

        categoryService.editCategory(category);
        return "redirect:/entries";
    }

    @DeleteMapping("/categories/delete/{id}")
    public String deleteCategoryById(@PathVariable("id") String id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/entries";
    }
}
