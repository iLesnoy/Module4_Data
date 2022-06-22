package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;

class TagControllerTest {

    private Tag tag = Tag.builder().id(1L)
            .name("test").build();
    private Tag tag2 = Tag.builder().id(2L)
            .name("aduditTest").build();
    @Test
    void findById() {

        given().log().body()
                .contentType("application/json").body(tag)
                .when().get("http://localhost:8080/api/tags/1")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void findByIdWhichNotExist() {

        given().log().body()
                .contentType("application/json").body(tag)
                .when().get("http://localhost:8080/api/tags/999999")
                .then().log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void findAll() {

        given().log().body()
                .contentType("application/json").body(List.of(tag,tag2))
                .when().get("http://localhost:8080/api/tags/?page=1&size=1")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }
}