package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.model.GiftCertificate;
import com.epam.esm.gifts.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

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
                .when().get("http://localhost:8080/api/certificates/1")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void create(){

        given().log().body()
                .contentType("application/json").body(giftCertificate)
                .when().post("http://localhost:8080/api/certificates")
                .then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void update(){

        long id = giftCertificate.getId();
        given().pathParam("id", id).log()
                .body().contentType("application/json").body(giftCertificate)

                .when().put("http://localhost:8080/api/certificates/{id}")

                .then().log().body().statusCode(HttpStatus.OK.value())
                .and().body("name", equalTo("prf"));

    }

    @Test
    void updateForbidden(){

        long id = giftCertificate.getId();
        given().pathParam("id", id).log()
                .body().contentType("application/json").body(giftCertificate)

                .when().put("http://localhost:8080/api/certificates/{id}")

                .then().log().body().statusCode(HttpStatus.FORBIDDEN.value())
                .and().body("name", equalTo("prf"));

    }

    @Test
    void updateBadRequest(){

        long id = giftCertificate.getId();
        given().pathParam("id", id).log()
                .body().contentType("application/json").body(giftCertificate)

                .when().put("http://localhost:8080/api/certificates/{id}")

                .then().log().body().statusCode(HttpStatus.NOT_FOUND.value())
                .and().body("name", equalTo("prf"));

    }

    @Test
    void findByAttributes() {

        given().log().body()
                .contentType("application/json").body(giftCertificate)
                .when().get("http://localhost:8080/api/certificates?" +
                        "tagNameList=test,data&searchPart=description&sortingFieldList=id," +
                        "name&orderSort=desc&page=1&size=1")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void findByAttributesWhenPageIsNotExist() {

        given().log().body()
                .contentType("application/json").body(giftCertificate)
                .when().get("http://localhost:8080/api/certificates?" +
                        "tagNameList=test,data&searchPart=description&sortingFieldList=id," +
                        "name&orderSort=desc&page=10000&size=1")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}