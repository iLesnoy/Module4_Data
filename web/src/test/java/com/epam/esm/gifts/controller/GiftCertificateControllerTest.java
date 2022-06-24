package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.model.GiftCertificate;
import com.epam.esm.gifts.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static io.restassured.RestAssured.given;

class GiftCertificateControllerTest {

    private GiftCertificate giftCertificate = GiftCertificate.builder()
            .id(12L)
            .name("prf")
            .description("description")
            .price(new BigDecimal(1000))
            .duration(30)
            .createDate(LocalDateTime.now())
            .lastUpdateDate(LocalDateTime.now())
            .tagList(Set.of(Tag.builder()
                            .id(5L)
                            .name("pack").build(),
                    Tag.builder()
                            .id(1L)
                            .name("test")
                            .build()))
            .createDate(LocalDateTime.now()).build();


    @Test
    void findById() {

        given().log().body()
                .contentType("application/json").body(giftCertificate)
                .when().get("http://localhost:8085/gift_system/api/certificates/1")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void findByIdNotExist() {

        given().log().body()
                .contentType("application/json").body(giftCertificate)
                .when().get("http://localhost:8085/gift_system/api/certificates/10000000")
                .then().log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create(){

        given().log().body()
                .contentType("application/json").body(giftCertificate)
                .when().post("http://localhost:8085/gift_system/api/certificates/")
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void update(){

        long id = giftCertificate.getId();
        given().pathParam("id", 1).log()
                .body().contentType("application/json").body(giftCertificate)

                .when().put("http://localhost:8085/gift_system/api/certificates/{id}")
                .then().log().body().statusCode(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void updateForbidden(){

        long id = giftCertificate.getId();
        given().pathParam("id", id).log()
                .body().contentType("application/json").body(giftCertificate)

                .when().put("http://localhost:8085/gift_system/api/certificates/{id}")

                .then().log().body().statusCode(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void updateBadRequest(){

        long id = giftCertificate.getId();
        given().pathParam("id", id).log()
                .body().contentType("application/json").body(giftCertificate)
                .when().put("http://localhost:8085/gift_system/api/certificates/{id}")
                .then().log().body().statusCode(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void findByAttributes() {

        given().log().body()
                .contentType("application/json").body(giftCertificate)
                .when().get("http://localhost:8085/gift_system/api/certificates/?" +
                        "tagNameList=test,data&searchPart=description&sortingFieldList=id," +
                        "name&orderSort=desc&page=1&size=1")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void findByAttributesWhenPageIsNotExist() {

        given().log().body()
                .contentType("application/json").body(giftCertificate)
                .when().get("http://localhost:8085/gift_system/api/certificates/?" +
                        "tagNameList=test,data&searchPart=description&sortingFieldList=id," +
                        "name&orderSort=desc&page=10000&size=1")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}