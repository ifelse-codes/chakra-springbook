package com.example.bookapi;

import com.example.bookapi.model.Publisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PublisherControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createUpdateDeletePublisher() {
        Publisher publisher = new Publisher("O'Reilly Media", "USA", 1980, "https://oreilly.com");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        ResponseEntity<Publisher> createResponse = restTemplate.exchange(
                "/api/publishers",
                HttpMethod.POST,
                new HttpEntity<>(publisher, headers),
                Publisher.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Assertions.assertNotNull(createResponse.getBody());
        Assertions.assertNotNull(createResponse.getBody().getId());

        Long publisherId = createResponse.getBody().getId();

        ResponseEntity<Publisher> getResponse = restTemplate.getForEntity("/api/publishers/" + publisherId, Publisher.class);
        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Assertions.assertNotNull(getResponse.getBody());
        Assertions.assertEquals("O'Reilly Media", getResponse.getBody().getName());

        publisher.setWebsite("https://www.oreilly.com");
        ResponseEntity<Publisher> updateResponse = restTemplate.exchange(
                "/api/publishers/" + publisherId,
                HttpMethod.PUT,
                new HttpEntity<>(publisher, headers),
                Publisher.class
        );

        Assertions.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        Assertions.assertNotNull(updateResponse.getBody());
        Assertions.assertEquals("https://www.oreilly.com", updateResponse.getBody().getWebsite());

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/publishers/" + publisherId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }
}
