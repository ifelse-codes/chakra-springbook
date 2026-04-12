package com.example.bookapi.controller;

import com.example.bookapi.model.Publisher;
import com.example.bookapi.repository.PublisherRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherRepository publisherRepository;

    public PublisherController(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @GetMapping
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable("id") Long id) {
        return publisherRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher) {
        Publisher saved = publisherRepository.save(publisher);
        return ResponseEntity.created(URI.create("/api/publishers/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable("id") Long id, @RequestBody Publisher publisher) {
        return publisherRepository.findById(id)
                .map(existing -> {
                    existing.setName(publisher.getName());
                    existing.setCountry(publisher.getCountry());
                    existing.setFoundedYear(publisher.getFoundedYear());
                    existing.setWebsite(publisher.getWebsite());
                    return ResponseEntity.ok(publisherRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublisher(@PathVariable("id") Long id) {
        return publisherRepository.findById(id)
                .map(existing -> {
                    publisherRepository.delete(existing);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
