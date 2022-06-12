package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.model.Role;
import com.epam.esm.gifts.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

class UserControllerTest {


    private User user = User.builder().name("admin")
            .id(1L)
            .password("$2a$12$LlyfSzHKIgFfoufqFbEYNeY6ePGUZq9vKpnuqFgMO8dyNyOBGDNK.")
            .role(Role.ADMIN)
            .orderList(null).build();
    @Test
    void create() {

        given().log().body()
                .contentType("application/json").body(user)
                .when().post("http://localhost:8080/api/users")
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void createInvalidParam() {

        given().log().body()
                .contentType("application/json").body(user)
                .when().post("http://localhost:8080/api/users_@!")
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void findById() {

        given().log().body()
                .contentType("application/json")
                .body(user)
                .when().get("http://localhost:8080/api/users/1")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void findByName() {

        given().log().body()
                .contentType("application/json")
                .body(user)
                .when().get("http://localhost:8080/api/users/search/admin")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }


    @Test
    void delete() {
        long id = user.getId();
        given().pathParam("id", id).log()
                .body().contentType("application/json").body(user)
                .when()
                .delete("http://localhost:8080/api/users/{id}")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}