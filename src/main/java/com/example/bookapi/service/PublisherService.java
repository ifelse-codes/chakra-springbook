package com.example.bookapi.service;

import com.example.bookapi.model.Publisher;
import com.example.bookapi.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public Optional<Publisher> getPublisherById(Long id) {
        return publisherRepository.findById(id);
    }

    public Publisher createPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Optional<Publisher> updatePublisher(Long id, Publisher publisher) {
        return publisherRepository.findById(id)
                .map(existing -> {
                    existing.setName(publisher.getName());
                    existing.setCountry(publisher.getCountry());
                    existing.setFoundedYear(publisher.getFoundedYear());
                    existing.setWebsite(publisher.getWebsite());
                    existing.setContactEmail(publisher.getContactEmail());
                    return publisherRepository.save(existing);
                });
    }

    public boolean deletePublisher(Long id) {
        return publisherRepository.findById(id)
                .map(existing -> {
                    publisherRepository.delete(existing);
                    return true;
                })
                .orElse(false);
    }
}
