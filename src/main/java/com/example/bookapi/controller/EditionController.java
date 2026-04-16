package com.example.bookapi.controller;

import com.example.bookapi.model.Edition;
import com.example.bookapi.repository.EditionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/editions")
public class EditionController {

    private final EditionRepository editionRepository;

    public EditionController(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }

    @GetMapping
    public List<Edition> getAllEditions() {
        return editionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Edition> getEditionById(@PathVariable Long id) {
        return editionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Edition> createEdition(@RequestBody Edition edition) {
        Edition saved = editionRepository.save(edition);
        return ResponseEntity.created(URI.create("/api/editions/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Edition> updateEdition(@PathVariable Long id, @RequestBody Edition updatedEdition) {
        return editionRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updatedEdition.getTitle());
                    existing.setEditionNumber(updatedEdition.getEditionNumber());
                    existing.setPublicationYear(updatedEdition.getPublicationYear());
                    existing.setIsbn(updatedEdition.getIsbn());
                    return ResponseEntity.ok(editionRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEdition(@PathVariable Long id) {
        return editionRepository.findById(id)
                .map(existing -> {
                    editionRepository.delete(existing);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
