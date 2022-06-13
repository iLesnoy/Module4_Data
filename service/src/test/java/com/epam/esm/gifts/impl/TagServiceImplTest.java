package com.epam.esm.gifts.impl;

import com.epam.esm.gifts.converter.TagConverter;
import com.epam.esm.gifts.dao.GiftCertificateRepository;
import com.epam.esm.gifts.dao.TagRepository;
import com.epam.esm.gifts.dto.CustomPage;
import com.epam.esm.gifts.dto.TagDto;
import com.epam.esm.gifts.exception.SystemException;
import com.epam.esm.gifts.model.GiftCertificate;
import com.epam.esm.gifts.model.Tag;
import com.epam.esm.gifts.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl service;
    @Mock
    private TagRepository tagDao;
    @Mock
    private EntityValidator validator;
    @Mock
    private TagConverter tagConverter;
    @Mock
    private Pageable pageable;
    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    private Tag tag;
    private TagDto tagDto;
    private GiftCertificate giftCertificate;
    private Page<TagDto> tagPage;

    @BeforeEach
    void setUp() {
        giftCertificate = GiftCertificate.builder().id(1L).name("name").build();
        tag = Tag.builder().id(1L).name("name").build();
        tagDto = TagDto.builder().id(1L).name("name").build();
        tagPage = new CustomPage<>(List.of(tagDto, tagDto), pageable, 15L);
    }

    @Test
    void create() {
        doReturn(tag).when(tagConverter).dtoToTag(any(TagDto.class));
        doReturn(false).when(validator).isNameValid(anyString());
        SystemException thrown = assertThrows(SystemException.class, () -> service.create(tagDto));
        assertEquals(40020, thrown.getErrorCode());
    }

    @Test
    void createWithInvalidName() {
        doReturn(tag).when(tagConverter).dtoToTag(any(TagDto.class));
        doReturn(true).when(validator).isNameValid(anyString());
        doReturn(Optional.of(tag)).when(tagDao).findByName(anyString());
        doReturn(tagDto).when(tagConverter).tagToDto(any(Tag.class));
        TagDto actual = service.create(tagDto);
        assertEquals(tagDto, actual);
    }


    @Test
    void update() {
        doReturn(Optional.of(tag)).when(tagDao).findById(Mockito.anyLong());
        doReturn(tag).when(tagConverter).dtoToTag(any(TagDto.class));
        doReturn(true).when(validator).isNameValid(anyString());
        TagDto tag = service.update(1L, tagDto);
        assertEquals(tag, tagDto);
    }

    @Test
    void updateThrowInvalidName() {
        doReturn(false).when(validator).isNameValid(anyString());
        doReturn(tag).when(tagConverter).dtoToTag(any(TagDto.class));
        SystemException thrown = assertThrows(SystemException.class, () -> service.create(tagDto));
        assertEquals(40020, thrown.getErrorCode());
    }


    @Test
    void findAllPageNotExist() {
        doReturn(tagPage).when(tagDao).findAll(pageable);
        doReturn(false).when(validator).isPageExists(any(Pageable.class), anyLong());
        SystemException thrown = assertThrows(SystemException.class, () -> service.findAll(pageable));
        assertEquals(40051, thrown.getErrorCode());
    }


    @Test
    void findById() {
        doReturn(tagDto).when(tagConverter).tagToDto(any(Tag.class));
        doReturn(Optional.of(tag)).when(tagDao).findById(anyLong());
        TagDto actual = service.findById(1L);
        assertEquals(tagDto, actual);
    }

    @Test
    void findByIdThrowsExceptionWithNonExistentEntity() {
        doReturn(Optional.empty()).when(tagDao).findById(anyLong());
        SystemException thrown = assertThrows(SystemException.class, () -> service.findById(1L));
        assertEquals(40410, thrown.getErrorCode());
    }

    @Test
    void deleteThrowsExceptionWithNonExistentEntity() {
        doReturn(Optional.of(tag)).when(tagDao).findById(anyLong());
        doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findFirstByTagList_Id(Mockito.anyLong());
        SystemException thrown = assertThrows(SystemException.class, () -> service.delete(1L));
        assertEquals(40910, thrown.getErrorCode());
    }

    @Test
    void delete() {
        doReturn(Optional.of(tag)).when(tagDao).findById(anyLong());
        doReturn(Optional.empty()).when(giftCertificateRepository).findFirstByTagList_Id(Mockito.anyLong());
        doNothing().when(tagDao).delete(any(Tag.class));
        service.delete(1L);
        assertTrue(true);
    }
}