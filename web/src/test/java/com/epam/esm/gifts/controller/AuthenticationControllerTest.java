package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

class AuthenticationControllerTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L)
                .name("adminka")
                .password("admin123")
                .build();
    }

    @Test
    void signUpWithExistName() {
        given().log().body()
                .contentType("application/json").body(user)
                .when().post("http://localhost:8085/giftsystem/api/auth/signup")
                .then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void signUpWithEmptyName() {
        given().log().body()
                .contentType("application/json").body("")
                .when().post("http://localhost:8085/gift_system/api/auth/signup")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void signUpInvalidUserData() {
        user.setName("---====");
        given().log().body()
                .contentType("application/json").body(user)
                .when().post("http://localhost:8085/gift_system/api/auth/signup")
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }


    @Test
    void authenticateBadRequest() {
        given().log().body()
                .contentType("application/json").body(user)
                .when().post("http://localhost:8085/gift_system/api/auth/login")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticateInvalidUserData() {
        user.setPassword(null);
        given().log().body()
                .contentType("application/json").body(user)
                .when().post("http://localhost:8085/gift_system/api/auth/login")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticateInvalidUserDataBody() {
        user.setPassword(null);
        given().log().body()
                .contentType("application/json").body("")
                .when().post("http://localhost:8085/gift_system/api/auth/login")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}