package com.epam.esm.gifts.dao;

import com.epam.esm.gifts.model.GiftCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes= TagRepository.class)
@EnableJpaRepositories(basePackages = {"com.epam.esm.gifts.*"})
@EntityScan("com.epam.esm.gifts.model")
@ActiveProfiles("test")
@DataJpaTest
class GiftCertificateRepositoryTest {

    private GiftCertificate expected;

    @Autowired
    GiftCertificateRepository giftCertificateRepository;

    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        expected = GiftCertificate.builder()
                .id(1L).name("gift").description("test")
                .duration(2).createDate(LocalDateTime.MAX).price(new BigDecimal(400)).build();
    }

    @Test
    void create(){
        GiftCertificate actual = GiftCertificate.builder()
                .id(1L).name("gift").description("test")
                .duration(2).createDate(LocalDateTime.MAX).price(new BigDecimal(400)).build();
        assertEquals(actual.getPrice(), expected.getPrice());
    }

    @Test
    void delete(){
        giftCertificateRepository.delete(expected);
        assertTrue(true);
    }

    @Test
    void update(){
        expected.setName("updated");
        giftCertificateRepository.save(expected);
        assertEquals(giftCertificateRepository.findById(1L).get().getName(),"updated");
    }
}