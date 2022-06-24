package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;

class TagControllerTest {

    private final Tag tag = Tag.builder().id(1L)
            .name("test").build();
    private final Tag tag2 = Tag.builder().id(2L)
            .name("aduditTest").build();

    @Test
    void findById() {

        given().log().body()
                .contentType("application/json").body(tag)
                .when().get("http://localhost:8085/gift_system/api/tags/1")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void findByIdWhichNotExist() {

        given().log().body()
                .contentType("application/json").body(tag)
                .when().get("http://localhost:8085/gift_system/api/tags/999999")
                .then().log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void findAll() {
        given().log().body()
                .contentType("application/json").body(List.of(tag, tag2))
                .when().get("http://localhost:8085/gift_system/api/tags?page=1&size=1")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }


    @Test
    void delete() {
        given().pathParam("id", 1).log().body()
                .contentType("application/json")
                .delete("http://localhost:8085/gift_system/api/tags/{id}")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void deleteIfTagNotExist() {
        given().pathParam("id", 100000).log().body()
                .contentType("application/json")
                .delete("http://localhost:8085/gift_system/api/tags/{id}")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}