package com.epam.esm.gifts.converter;

import com.epam.esm.gifts.dto.GiftCertificateAttributeDto;
import com.epam.esm.gifts.model.GiftCertificateAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftCertificateAttributeConverterTest {

    private GiftCertificateAttributeConverter toGiftCertificateAttributeConverter;
    private GiftCertificateAttributeDto certificateAttributeDto;
    private GiftCertificateAttribute certificateAttribute;

    @BeforeEach
    public void setUp() {
        toGiftCertificateAttributeConverter = new GiftCertificateAttributeConverter();
        certificateAttributeDto = GiftCertificateAttributeDto.builder()
                .tagNameList(List.of("nameOne", "second", "third", "fourth"))
                .searchPart("searchPart")
                .sortingFieldList(List.of("id", "name", "description", "price", "duration", "createDate", "date"))
                .orderSort("desc")
                .build();
        certificateAttribute = GiftCertificateAttribute.builder()
                .tagNameList(List.of("nameOne", "second", "third", "fourth"))
                .searchPart("searchPart")
                .sortingFieldList(List.of("id", "name", "description", "price", "duration", "createDate", "date"))
                .orderSort("desc")
                .build();
    }

    @Test
    void convert() {
        GiftCertificateAttribute actual = toGiftCertificateAttributeConverter.convert(certificateAttributeDto);
        assertEquals(certificateAttribute, actual);
    }
}