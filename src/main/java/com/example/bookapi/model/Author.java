package com.example.bookapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String bio;
    private String email;
    private String website;
    private String nationality;
    private String birthPlace;

    public Author() {
    }

    public Author(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }

    public Author(String name, String bio, String email) {
        this.name = name;
        this.bio = bio;
        this.email = email;
    }

    public Author(String name, String bio, String email, String website) {
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.website = website;
    }

    public Author(String name, String bio, String email, String website, String nationality) {
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.website = website;
        this.nationality = nationality;
    }

    public Author(String name, String bio, String email, String website, String nationality, String birthPlace) {
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.website = website;
        this.nationality = nationality;
        this.birthPlace = birthPlace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }
}
