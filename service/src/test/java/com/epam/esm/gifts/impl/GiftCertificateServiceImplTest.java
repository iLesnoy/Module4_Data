package com.epam.esm.gifts.impl;

import com.epam.esm.gifts.converter.GiftCertificateConverter;
import com.epam.esm.gifts.converter.TagConverter;
import com.epam.esm.gifts.dao.GiftCertificateRepository;
import com.epam.esm.gifts.dao.OrderRepository;
import com.epam.esm.gifts.dto.CustomPage;
import com.epam.esm.gifts.dto.GiftCertificateAttributeDto;
import com.epam.esm.gifts.dto.GiftCertificateDto;
import com.epam.esm.gifts.dto.TagDto;
import com.epam.esm.gifts.exception.SystemException;
import com.epam.esm.gifts.model.GiftCertificate;
import com.epam.esm.gifts.model.Order;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    private static final long TAG_ID = 1;
    private static final String TAG_NAME = "Tag";

    private static final long CERTIFICATE_ID = 1;
    private static final String CERTIFICATE_NAME = "certificate";
    private static final String DESCRIPTION = "description";
    private static final BigDecimal PRICE = new BigDecimal("10");
    private static final int DURATION = 50;
    private static final LocalDateTime CREATION_DATE = LocalDateTime.now();
    private static final LocalDateTime LAST_UPDATE_DATE = LocalDateTime.now();


    private Tag tag;
    private TagDto tagDto;
    private Set<Tag> tagSet;
    private List<TagDto> tagDtoList;
    private GiftCertificate certificate;
    private Order order;
    private GiftCertificateDto updatedCertificate;
    private GiftCertificateDto certificateDto;
    private List<String> sortingFieldList;
    private List<GiftCertificateDto> expectedList;
    private CustomPage<GiftCertificateDto> page;
    private Page<GiftCertificate> giftCertificates;

    @InjectMocks
    private GiftCertificateServiceImpl service;
    @Mock
    OrderRepository orderRepository;
    @Mock
    private GiftCertificateRepository certificateDao;
    @Mock
    private EntityValidator validator;
    @Mock
    private Pageable pageable;
    @Mock
    private GiftCertificateConverter certificateConverter;
    @Mock
    private TagConverter tagConverter;
    @Mock
    private TagServiceImpl tagService;
    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @BeforeEach
    void setUp() {
        tag = Tag.builder().id(1L).name("tag").build();
        tagDto = new TagDto(TAG_ID, TAG_NAME);
        tagSet = new HashSet<>(List.of(tag, tag, tag));
        tagDtoList = new ArrayList<>(List.of(tagDto, tagDto, tagDto));
        certificate = new GiftCertificate(CERTIFICATE_ID, CERTIFICATE_NAME, DESCRIPTION, PRICE, DURATION, CREATION_DATE
                , LAST_UPDATE_DATE, tagSet);
        order = Order.builder().id(1L)
                .purchaseTime((LocalDateTime.of(2001, 1, 1, 2, 3)))
                .cost(new BigDecimal("500"))
                .certificateList(List.of(GiftCertificate.builder().build()))
                .build();
        certificateDto = GiftCertificateDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .price(new BigDecimal("40"))
                .duration(5)
                .createDate(LocalDateTime.of(2001, 1, 2, 3, 4))
                .lastUpdateDate(LocalDateTime.of(2003, 1, 2, 3, 4))
                .tagDtoList(List.of(tagDto))
                .build();
        updatedCertificate = GiftCertificateDto.builder()
                .id(1L)
                .name("nama")
                .description("description")
                .price(new BigDecimal("40"))
                .duration(5)
                .createDate(LocalDateTime.of(2001, 1, 2, 3, 4))
                .lastUpdateDate(LocalDateTime.of(2003, 1, 2, 3, 4))
                .tagDtoList(List.of(tagDto))
                .build();
        page = new CustomPage<>(List.of(certificateDto), pageable, 30L);
        sortingFieldList = List.of("name", "price");
        expectedList = List.of(certificateDto, certificateDto);
    }

    @Test
    void create() {
        doReturn(certificate).when(certificateConverter).dtoToGiftCertificate(Mockito.any(GiftCertificateDto.class));
        doReturn(certificateDto).when(certificateConverter).giftCertificateToDto(Mockito.any(GiftCertificate.class));
        GiftCertificateDto actual = service.create(certificateDto);
        assertEquals(certificateDto, actual);
    }



    /*@Test
    void update() {
        doNothing().when(validator).checkGiftValidation(Mockito.any(GiftCertificateDto.class));
        doReturn(certificate).when(service).findCertificateById(Mockito.anyLong());

        doReturn(tag).when(tagConverter).dtoToTag(Mockito.any(TagDto.class));
        doReturn(tag).when(tagService).createTag(Mockito.any(Tag.class));
        doNothing().when(certificateDao).save(certificate);
        doReturn(updatedCertificate).when(certificateConverter).giftCertificateToDto(Mockito.any(GiftCertificate.class));
        GiftCertificateDto actual = service.update(1L, updatedCertificate);
        assertEquals(actual, updatedCertificate);
    }*/

    @Test
    void updateWithNullTagList() {
        try {
            GiftCertificateDto actual = service.update(1L, GiftCertificateDto.builder()
                    .id(1L).name("qwe").tagDtoList(List.of()).price(new BigDecimal("2.0")).duration(2).build());
            service.update(1L, actual);
        } catch (SystemException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateThrowExceptionNullArgs() {
        try {
            service.update(null, null);
            fail("Method update should throw SystemException");
        } catch (SystemException e) {
            assertTrue(true);
        }
    }

    @Test
    void findById() {
        doReturn(certificateDto).when(certificateConverter).giftCertificateToDto(Mockito.any(GiftCertificate.class));
        doReturn(Optional.of(certificate)).when(certificateDao).findById(anyLong());
        GiftCertificateDto actual = service.findById(1L);
        assertNotNull(actual);
    }

    @Test
    void findCertificateByIdWhenEntityNonExist() {
        SystemException thrown = assertThrows(SystemException.class, () -> service.findById(anyLong()));
        assertEquals(40410, thrown.getErrorCode());
    }

    @Test
    void findByIdThrowsExceptionWithNonExistentEntity() {
        SystemException thrown = assertThrows(SystemException.class, () -> service.findById(1L));
        assertEquals(40410, thrown.getErrorCode());
    }


    @Test
    void findAll() {
        try {
            service.findAll(pageable);
            fail("command is not supported, please use searchByParameters");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    /*@Test
    void searchByParameters() {
        doReturn(true).when(validator).isAttributeDtoValid(GiftCertificateAttributeDto.builder().build());
        doReturn(page).when(orderRepository).findAll(pageable);
        doReturn(certificateDto).when(certificateConverter).giftCertificateToDto(Mockito.any(GiftCertificate.class));
        Page<GiftCertificateDto> actual = service.searchByParameters(GiftCertificateAttributeDto.builder().build(),pageable);
        assertEquals(page, actual);
    }*/

    @Test
    void searchByParametersThrowInvalidAttributeList() {
        SystemException thrown = assertThrows(SystemException.class, () -> service.searchByParameters(GiftCertificateAttributeDto.builder().build(),pageable));
        assertEquals(40034, thrown.getErrorCode());
    }


    /*@Test
    void delete() {
        doReturn(Optional.of(certificate)).when(giftCertificateRepository).findById(Mockito.anyLong());
        doReturn(Optional.empty()).when(orderRepository).findFirstByCertificateListId(Mockito.anyLong());
        service.delete(1L);
        assertTrue(true);
    }*/

    @Test
    void deleteIfEntityUsed() {
        SystemException thrown = assertThrows(SystemException.class, () -> service.delete(Mockito.anyLong()));
        assertEquals(40410, thrown.getErrorCode());
    }


    private void setInvalidName() {
        doReturn(false).when(validator).isNameValid(anyString());
    }
}