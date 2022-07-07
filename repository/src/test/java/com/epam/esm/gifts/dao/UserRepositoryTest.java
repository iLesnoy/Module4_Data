package com.epam.esm.gifts.dao;

import com.epam.esm.gifts.model.Role;
import com.epam.esm.gifts.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes= TagRepository.class)
@EnableJpaRepositories(basePackages = {"com.epam.esm.gifts.*"})
@EntityScan("com.epam.esm.gifts.model")
@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    private User expected;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        expected = User.builder().id(1L)
                .name("user").password("123123")
                .role(Role.USER).build();
    }

    @Test
    void create(){
        User actual = User.builder().id(1L)
                .name("user").password("123123")
                .role(Role.USER).build();
        assertEquals(actual.getPassword(), expected.getPassword());
    }

    @Test
    void delete(){
        userRepository.delete(expected);
        assertTrue(true);
    }

    @Test
    void update(){
        expected.setName("VASILIY");
        userRepository.save(expected);
        assertNotNull(userRepository.findByName("VASILIY"));
    }

}