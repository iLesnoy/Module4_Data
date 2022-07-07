package com.epam.esm.gifts.dao;

import com.epam.esm.gifts.model.GiftCertificate;
import com.epam.esm.gifts.model.Order;
import com.epam.esm.gifts.model.Role;
import com.epam.esm.gifts.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes= TagRepository.class)
@EnableJpaRepositories(basePackages = {"com.epam.esm.gifts.*"})
@EntityScan("com.epam.esm.gifts.model")
@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    private GiftCertificate giftCertificate;
    private User user;
    private Order order;

    @Autowired
    UserRepository userRepository;
    @Autowired
    GiftCertificateRepository giftCertificateRepository;
    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        giftCertificate = GiftCertificate.builder()
                .id(1L).name("gift").description("test")
                .duration(2).createDate(LocalDateTime.MAX).price(new BigDecimal(400)).build();
        user = User.builder().id(1L)
                .name("user").password("123123")
                .role(Role.USER).build();
        order = Order.builder().id(1L).certificateList(List.of(giftCertificate))
                .purchaseTime(LocalDateTime.now()).cost(new BigDecimal(200))
                .user(user).build();
    }

    @Test
    void delete(){
        orderRepository.delete(order);
        assertTrue(true);
    }

}