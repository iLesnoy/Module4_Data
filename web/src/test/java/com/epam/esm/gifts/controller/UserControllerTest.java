package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.model.Role;
import com.epam.esm.gifts.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

class UserControllerTest {


    private final User user = User.builder().name("admin")
            .id(1L)
            .password("$udasdae123123DEW")
            .role(Role.ADMIN)
            .orderList(null).build();

    @Test
    void create() {

        given().log().body()
                .contentType("application/json").body(user)
                .when().post("http://localhost:8085/gift_system/api/auth/signup")
                .then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void createInvalidParam() {

        given().log().body()
                .contentType("application/json").body(user)
                .when().post("http://localhost:8085/gift_system/api/users_@!")
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void findById() {

        given().log().body()
                .contentType("application/json")
                .body(user)
                .when().get("http://localhost:8085/gift_system/api/users/1")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void findByNotExistId() {

        given().log().body()
                .contentType("application/json")
                .body(user)
                .when().get("http://localhost:8085/gift_system/api/users/10000000")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void findByName() {

        given().log().body()
                .contentType("application/json")
                .body(user)
                .when().get("http://localhost:8085/gift_system/api/users/admin")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }


    @Test
    void delete() {
        given().pathParam("id",1)
                .when()
                .delete("http://localhost:8085/gift_system/api/users/{id}")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}