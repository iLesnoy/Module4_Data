package com.epam.esm.gifts.model;

import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Data
@Builder
public class GiftCertificateAttribute {

    private List<String> tagNameList;
    private String searchPart;
    private String orderSort;
    private List<String> sortingFieldList;


}