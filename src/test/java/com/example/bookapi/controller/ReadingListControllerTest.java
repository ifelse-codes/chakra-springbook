package com.example.bookapi.controller;

import com.example.bookapi.model.ReadingList;
import com.example.bookapi.repository.ReadingListRepository;
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
class ReadingListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReadingListRepository readingListRepository;

    @BeforeEach
    void setUp() {
        readingListRepository.deleteAll();
    }

    @Test
    void createAndFetchReadingList() throws Exception {
        mockMvc.perform(post("/api/reading-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Spring Books\",\"description\":\"Books to learn Spring Boot\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern("/api/reading-lists/\\d+")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is("Spring Books")))
                .andExpect(jsonPath("$.description", is("Books to learn Spring Boot")));

        Long createdId = readingListRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/reading-lists/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Spring Books")));
    }

    @Test
    void updateReadingList() throws Exception {
        ReadingList saved = readingListRepository.save(new ReadingList("Old Name", "Old Description"));

        mockMvc.perform(put("/api/reading-lists/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Name\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.description", is("Updated Description")));
    }

    @Test
    void deleteReadingList() throws Exception {
        ReadingList saved = readingListRepository.save(new ReadingList("Delete Me", "Temporary list"));

        mockMvc.perform(delete("/api/reading-lists/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/reading-lists/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}
