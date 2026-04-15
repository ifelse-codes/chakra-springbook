package com.example.bookapi.controller;

import com.example.bookapi.model.Book;
import com.example.bookapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void createAndFetchBook() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Spring in Action\",\"author\":\"Craig Walls\",\"isbn\":\"9781617294945\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern("/api/books/\\d+")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title", is("Spring in Action")))
                .andExpect(jsonPath("$.author", is("Craig Walls")))
                .andExpect(jsonPath("$.isbn", is("9781617294945")));

        Long createdId = bookRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/books/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Spring in Action")));
    }

    @Test
    void updateBook() throws Exception {
        Book saved = bookRepository.save(new Book("Old Title", "Old Author", "111"));

        mockMvc.perform(put("/api/books/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Title\",\"author\":\"New Author\",\"isbn\":\"222\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Title")))
                .andExpect(jsonPath("$.author", is("New Author")))
                .andExpect(jsonPath("$.isbn", is("222")));
    }

    @Test
    void deleteBook() throws Exception {
        Book saved = bookRepository.save(new Book("Delete Me", "Author", "333"));

        mockMvc.perform(delete("/api/books/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/books/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}
