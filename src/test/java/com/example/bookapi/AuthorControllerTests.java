package com.example.bookapi;

import com.example.bookapi.model.Author;
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
class AuthorControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndGetAuthor() {
        Author author = new Author(
                "Eric Evans",
                "Domain-driven design expert",
                "eric.evans@example.com",
                "https://example.com",
                "British",
                "London"
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        ResponseEntity<Author> createResponse = restTemplate.exchange(
                "/api/authors",
                HttpMethod.POST,
                new HttpEntity<>(author, headers),
                Author.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Assertions.assertNotNull(createResponse.getBody());
        Assertions.assertNotNull(createResponse.getBody().getId());

        Long createdId = createResponse.getBody().getId();
        ResponseEntity<Author> getResponse = restTemplate.getForEntity("/api/authors/" + createdId, Author.class);

        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Assertions.assertNotNull(getResponse.getBody());
        Assertions.assertEquals("Eric Evans", getResponse.getBody().getName());
        Assertions.assertEquals("Domain-driven design expert", getResponse.getBody().getBio());
        Assertions.assertEquals("eric.evans@example.com", getResponse.getBody().getEmail());
        Assertions.assertEquals("https://example.com", getResponse.getBody().getWebsite());
        Assertions.assertEquals("British", getResponse.getBody().getNationality());
        Assertions.assertEquals("London", getResponse.getBody().getBirthPlace());
    }
}
