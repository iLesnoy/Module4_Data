package com.epam.esm.gifts.dao;

import com.epam.esm.gifts.model.Tag;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.SystemException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes= TagRepository.class)
@EnableJpaRepositories(basePackages = {"com.epam.esm.gifts.*"})
@EntityScan("com.epam.esm.gifts.model")
@ActiveProfiles("test")
@DataJpaTest
class TagRepositoryTest {

    private Tag expected;
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        expected = Tag.builder().id(1L).name("tag1").build();
    }

    @Test
    void create() {
        Tag tag = tagRepository.save(expected);
        Assertions.assertTrue(tag!=null);
    }


    @Test
    void delete(){
        tagRepository.delete(expected);
        Assertions.assertTrue(true);
    }

    @Test
    void update(){
        expected.setName("upd");
        tagRepository.save(expected);
        assertNotNull(tagRepository.findByName("upd"));
    }
}