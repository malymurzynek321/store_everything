package org.example.store_everything.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.store_everything.models.Category;
import org.example.store_everything.models.Information;
import org.example.store_everything.models.User;
import org.example.store_everything.repositories.InformationRepository;
import org.example.store_everything.services.CategoryService;
import org.example.store_everything.services.CustomUserDetailsService;
import org.example.store_everything.services.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/entries")
public class InformationController {

    private final CategoryService categoryService;
    private final InformationService informationService;
    private final InformationRepository informationRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public InformationController(CategoryService categoryService,
                                 InformationService informationService,
                                 InformationRepository informationRepository,
                                 CustomUserDetailsService customUserDetailsService) {
        this.categoryService = categoryService;
        this.informationService = informationService;
        this.informationRepository = informationRepository;
        this.customUserDetailsService = customUserDetailsService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Category.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Category category = categoryService.getCategoryById(text);
                setValue(category);
            }
        });
    }

    @GetMapping
    public String gallery(Model model, HttpSession session) {
        String sortBy = (String) session.getAttribute("sortBy");
        String sortDirection = (String) session.getAttribute("sortDirection");

        if (sortBy == null) {
            sortBy = "date";
            session.setAttribute("sortBy", sortBy);
        }
        if (sortDirection == null) {
            sortDirection = "desc";
            session.setAttribute("sortDirection", sortDirection);
        }

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Information> entries = informationService.getEntriesByOwnerId(principal.getId());
        List<Category> categories = categoryService.getCategoriesByOwnerId(principal.getId());

        Map<String, Long> categoryCounts = new HashMap<>();
        Map<String, String> categoryIdToNameMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        for (Category c : categories) {
            long count = informationRepository.countByCategoryId(c.getId());
            categoryCounts.put(c.getId(), count);
        }

        model.addAttribute("entries", entries);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryCounts", categoryCounts);
        model.addAttribute("categoryIdToNameMap", categoryIdToNameMap);
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        return "entries/entries";
    }

    @PostMapping("/sort")
    public String sortEntries(@RequestParam String sortBy, @RequestParam String sortDirection, HttpSession session) {
        session.setAttribute("sortBy", sortBy);
        session.setAttribute("sortDirection", sortDirection);
        return "redirect:/entries";
    }

    @PostMapping("/filter/date")
    public String filterEntriesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, Model model) {
        List<Information> entriesFilteredByDate = informationService.getEntriesFilteredByDate(fromDate);
        model.addAttribute("entries", entriesFilteredByDate);
        return "entries/entries";
    }

    @PostMapping("/filter/category")
    public String filterEntriesByCategory(@RequestParam String categoryId, Model model) {
        List<Information> entriesFilteredByCategory = informationService.getEntriesFilteredByCategory(categoryId);
        model.addAttribute("entries", entriesFilteredByCategory);
        return "entries/entries";
    }
    @GetMapping("/{url}")
    public String getInformationByUrl(@PathVariable("url") String url, Model model) {
        Information information = informationService.getInformationByUrl(url);

        if (information == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nie znaleziono wpisu o linku '" + url + "'."
            );
        }

        model.addAttribute("information", information);
        model.addAttribute("userService", customUserDetailsService);
        return "/entries/entry";
    }

    @GetMapping("/create")
    public String showInformationCreationForm(Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("information", new Information());
        model.addAttribute("categories", categoryService.getCategoriesByOwnerId(principal.getId()));
        return "/entries/create";
    }

    @PostMapping("/create")
    public String createInformation(@Valid @ModelAttribute Information information, BindingResult result, Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getCategoriesByOwnerId(principal.getId()));
            return "/entries/create";
        }

        information.setOwnerId(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        informationService.createInformation(information);

        return "redirect:/entries";
    }

    @GetMapping("/edit/{id}")
    public String editInformationById(@PathVariable("id") String id, Model model) {
        Information information = informationService.getInformationById(id);

        if (information == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nie znaleziono wpisu o ID '" + id + "'."
            );
        }

        model.addAttribute("information", information);
        model.addAttribute("informationId", id);

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("categories", categoryService.getCategoriesByOwnerId(principal.getId()));

        return "/entries/edit";
    }

    @PostMapping("/edit")
    public String showInformationEditingForm(@Valid @ModelAttribute Information information, BindingResult result, Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getCategoriesByOwnerId(principal.getId()));
            return "/entries/edit";
        }

        Information oldInformation = informationService.getInformationById(information.getId());
        oldInformation.setTitle(information.getTitle());
        oldInformation.setContent(information.getContent());
        oldInformation.setCategoryId(information.getCategoryId());

        informationService.editInformation(oldInformation);
        return "redirect:/entries";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteInformationById(@PathVariable("id") String id) {
        informationService.deleteInformationById(id);
        return "redirect:/entries";
    }

}