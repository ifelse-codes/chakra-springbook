package com.example.bookapi;

import com.example.bookapi.model.Quote;
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
class QuoteControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndManageQuote() {
        Quote quote = new Quote("Simplicity is the ultimate sophistication.", "Leonardo da Vinci", "philosophy");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        ResponseEntity<Quote> createResponse = restTemplate.exchange(
                "/api/quotes",
                HttpMethod.POST,
                new HttpEntity<>(quote, headers),
                Quote.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Assertions.assertNotNull(createResponse.getBody());
        Assertions.assertNotNull(createResponse.getBody().getId());

        Long createdId = createResponse.getBody().getId();
        ResponseEntity<Quote> getResponse = restTemplate.getForEntity("/api/quotes/" + createdId, Quote.class);

        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Assertions.assertNotNull(getResponse.getBody());
        Assertions.assertEquals("Leonardo da Vinci", getResponse.getBody().getAuthor());

        quote.setText("Simplicity is the ultimate sophistication. Updated.");
        ResponseEntity<Quote> updateResponse = restTemplate.exchange(
                "/api/quotes/" + createdId,
                HttpMethod.PUT,
                new HttpEntity<>(quote, headers),
                Quote.class
        );

        Assertions.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        Assertions.assertNotNull(updateResponse.getBody());
        Assertions.assertEquals("Simplicity is the ultimate sophistication. Updated.", updateResponse.getBody().getText());

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/quotes/" + createdId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }
}
