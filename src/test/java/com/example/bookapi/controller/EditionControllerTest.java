package com.example.bookapi.controller;

import com.example.bookapi.model.Edition;
import com.example.bookapi.repository.EditionRepository;
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
class EditionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EditionRepository editionRepository;

    @BeforeEach
    void setUp() {
        editionRepository.deleteAll();
    }

    @Test
    void createAndFetchEdition() throws Exception {
        mockMvc.perform(post("/api/editions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Spring in Action\",\"editionNumber\":6,\"publicationYear\":2022,\"isbn\":\"9781617294945\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern("/api/editions/\\d+")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title", is("Spring in Action")))
                .andExpect(jsonPath("$.editionNumber", is(6)))
                .andExpect(jsonPath("$.publicationYear", is(2022)))
                .andExpect(jsonPath("$.isbn", is("9781617294945")));

        Long createdId = editionRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/editions/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Spring in Action")))
                .andExpect(jsonPath("$.isbn", is("9781617294945")));
    }

    @Test
    void updateEdition() throws Exception {
        Edition saved = editionRepository.save(new Edition("Old Edition", 1, 2018, "111"));

        mockMvc.perform(put("/api/editions/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Edition\",\"editionNumber\":2,\"publicationYear\":2024,\"isbn\":\"222\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Edition")))
                .andExpect(jsonPath("$.editionNumber", is(2)))
                .andExpect(jsonPath("$.publicationYear", is(2024)))
                .andExpect(jsonPath("$.isbn", is("222")));
    }

    @Test
    void deleteEdition() throws Exception {
        Edition saved = editionRepository.save(new Edition("Delete Me", 3, 2020, "333"));

        mockMvc.perform(delete("/api/editions/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/editions/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}
