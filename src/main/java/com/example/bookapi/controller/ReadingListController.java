package com.example.bookapi.controller;

import com.example.bookapi.model.ReadingList;
import com.example.bookapi.repository.ReadingListRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reading-lists")
public class ReadingListController {

    private final ReadingListRepository readingListRepository;

    public ReadingListController(ReadingListRepository readingListRepository) {
        this.readingListRepository = readingListRepository;
    }

    @GetMapping
    public List<ReadingList> getAllReadingLists() {
        return readingListRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadingList> getReadingListById(@PathVariable Long id) {
        return readingListRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReadingList> createReadingList(@RequestBody ReadingList readingList) {
        ReadingList saved = readingListRepository.save(readingList);
        return ResponseEntity.created(URI.create("/api/reading-lists/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadingList> updateReadingList(@PathVariable Long id,
                                                         @RequestBody ReadingList updatedReadingList) {
        return readingListRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedReadingList.getName());
                    existing.setDescription(updatedReadingList.getDescription());
                    return ResponseEntity.ok(readingListRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReadingList(@PathVariable Long id) {
        return readingListRepository.findById(id)
                .map(existing -> {
                    readingListRepository.delete(existing);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
