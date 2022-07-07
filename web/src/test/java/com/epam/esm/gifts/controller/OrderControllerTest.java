package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.hateaos.HateoasBuilder;
import com.epam.esm.gifts.model.Order;
import com.epam.esm.gifts.model.Role;
import com.epam.esm.gifts.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    private Order order;
    private User user;

    @Autowired
    OrderController orderController;
    @Autowired
    HateoasBuilder hateoasBuilder;

    @BeforeEach
    void setUp() {
        user = User.builder().name("admin")
                .id(1L)
                .password("$udasdae123123DEW")
                .role(Role.ADMIN)
                .orderList(null).build();
        order = Order.builder().id(1L)
                .user(user).cost(new BigDecimal(400))
                .build();
    }

    @Test
    void findAll() {
        given().log().body()
                .contentType("application/json").body(List.of(order))
                .when().get("http://localhost:8085/gift_system/api/orders?page=1&size=1")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void findById() {
        given().pathParam("id",1).log().body()
                .contentType("application/json")
                .when().get("http://localhost:8085/gift_system/api/orders/{id}")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }


    @Test
    void create() {
        given().log().body()
                .contentType("application/json").body(order)
                .when().post("http://localhost:8085/gift_system/api/orders")
                .then().log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

}