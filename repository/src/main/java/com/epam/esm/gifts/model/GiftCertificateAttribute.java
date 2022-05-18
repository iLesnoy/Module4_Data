package com.epam.esm.gifts.model;

import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Data
@Builder
public class GiftCertificateAttribute {
    private static final String DEFAULT_SORT = "ASC";
    private static final String DEFAULT_TAG_NAME_CONDITION = "tags.name LIKE % AND ";
    private static final String DEFAULT_SEARCH_PART_CONDITION = "(gift_certificates.name LIKE %)";
    private static final String DEFAULT_ORDER_CONDITION = "gift_certificates.id" + DEFAULT_SORT;

    private List<String> tagNameList;
    private String searchPart;
    private String orderSort;
    private List<String> sortingFieldList;

    public String buildQueryPartForTagNameList() {
        return CollectionUtils.isEmpty(tagNameList)
                ? DEFAULT_TAG_NAME_CONDITION
                : tagNameList.stream().reduce("", (result, next) -> result + "tags.name LIKE %" + next + "% AND ");
    }

    public String buildQueryPartForSearchPart() {
        return Objects.nonNull(searchPart)
                ? "(gift_certificates.name LIKE CONCAT (%" + searchPart + "%) OR gift_certificates.description LIKE CONCAT (%" + searchPart + "%)"
                : DEFAULT_SEARCH_PART_CONDITION;
    }

    public String buildOrderByFields() {
        orderSort = Objects.nonNull(orderSort) ? orderSort : DEFAULT_SORT;
        return CollectionUtils.isEmpty(sortingFieldList)
                ? DEFAULT_ORDER_CONDITION
                : sortingFieldList.stream().reduce("", (result, next) -> result + next + " " + orderSort);
    }
}