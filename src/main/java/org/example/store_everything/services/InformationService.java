package org.example.store_everything.services;

import org.example.store_everything.models.Information;
import org.example.store_everything.repositories.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class InformationService {

    private final InformationRepository informationRepository;

    @Autowired
    public InformationService(InformationRepository informationRepository) {
        this.informationRepository = informationRepository;
    }

    public Information getInformationById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID informacji nie może być null-em.");
        }
        return informationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono informacji o ID '" + id + "'."));
    }

    public Information getInformationByUrl(String url) {
        return informationRepository.findByUrl(url)
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono informacji z linkiem '" + url + "'."));
    }

    public void createInformation(Information information) {
        information.setCreationDate(LocalDate.now());

        String uniqueURL = UUID.randomUUID().toString();
        information.setUrl(uniqueURL);

        informationRepository.save(information);
    }

    public void editInformation(Information information) {
        informationRepository.save(information);
    }

    public void deleteInformationById(String id) {
        informationRepository.deleteById(id);
    }

    public List<Information> getEntriesByOwnerId(String userId) {
        return informationRepository.findByOwnerId(userId);
    }

    public List<Information> getEntriesFilteredByDate(LocalDate fromDate) {
        return informationRepository.findAll().stream()
                .filter(info -> info.getCreationDate().isAfter(fromDate))
                .toList();
    }

    public List<Information> getEntriesFilteredByCategory(String categoryId) {
        return informationRepository.findAll().stream()
                .filter(info -> info.getCategoryId().equals(categoryId))
                .toList();
    }
}