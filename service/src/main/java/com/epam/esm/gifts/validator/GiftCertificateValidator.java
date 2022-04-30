package com.epam.esm.gifts.validator;

import com.epam.esm.gifts.dto.*;
import com.epam.esm.gifts.exception.SystemException;
import com.epam.esm.gifts.model.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.epam.esm.gifts.exception.ExceptionCode.*;

@Component
public class GiftCertificateValidator {

    private static final int MIN_PERIOD = 1;
    private static final int MAX_PERIOD = 365;
    private static final String NAME_REGEX = "[\\p{Alpha}А-Яа-я]{2,65}";
    private static final String PRICE_REGEX = "^(\\d+|[\\.\\,]?\\d+){1,2}$";
    private static final String DESCRIPTION_REGEX = "[\\p{Alpha}А-Яа-я\\d-.,:;!?()\" ]{2,225}";
    private static final Set<String> AVAILABLE_SORT_ORDERS = Set.of("asc", "desc");
    private static final Set<String> AVAILABLE_SORTING_FIELDS = Set.of("id", "name","description"
    ,"price","duration","createDate","lastUpdateDate");
    private static final String PAGE_REGEX = "\\d+";


    private static boolean isNotNullAndBlank(String field) {
        return Objects.nonNull(field) && !field.isBlank();
    }

    public static boolean isTagListValid(List<TagDto> tagDtoList) {
        if (!CollectionUtils.isEmpty(tagDtoList) && isTagNameListValid(tagDtoList)) {
            return true;
        } else if(tagDtoList == null){
            return false;
        } else return CollectionUtils.isEmpty(tagDtoList) && !isTagNameListValid(tagDtoList);
    }


    private static boolean isTagNameListValid(List<TagDto> tagDtoList) {
        return tagDtoList.stream().allMatch(tag -> Objects.nonNull(tag) && isNameValid(tag.getName()));
    }

    public boolean isAttributeDtoValid(GiftCertificateAttributeDto attributeDto) {
        List<String> tagNameList = attributeDto.getTagNameList();
        String searchPart = attributeDto.getSearchPart();
        String orderSort = attributeDto.getOrderSort();
        List<String> sortingFieldList = attributeDto.getSortingFieldList();

        return (CollectionUtils.isEmpty(tagNameList) || tagNameList.stream()
                .allMatch(tagName -> Objects.nonNull(tagName) && isNameValid(tagName)))
                && isDescriptionValid(searchPart)
                && (Objects.isNull(sortingFieldList) || AVAILABLE_SORTING_FIELDS.contains(searchPart)
                && (Objects.isNull(orderSort) || AVAILABLE_SORT_ORDERS.contains(orderSort.toLowerCase())));
    }

    public static boolean isPriceValid(BigDecimal price) {
        return price == null ? Objects.nonNull(price) && matchPriceToRegex(price)
                : Objects.isNull(price) || matchPriceToRegex(price);
    }

    public static boolean isRequestOrderDataValid(RequestOrderDto orderDto) {
        return Objects.nonNull(orderDto.getUserId()) && Objects.nonNull(orderDto.getCertificateIdList())
                && orderDto.getCertificateIdList().stream().allMatch(Objects::nonNull);
    }


    public boolean isPageDataValid(CustomPageable pageable) {
        Integer size = pageable.getSize();
        Integer page = pageable.getPage();
        return  Objects.nonNull(page) && Objects.nonNull(size) && size != 0 && checkNumber(page) && checkNumber(size);
    }

    private boolean checkNumber(Number number) {
        return String.valueOf(number).matches(PAGE_REGEX);
    }

    public boolean isPageExists(CustomPageable pageable, Long totalNumber) {
        if (pageable.getPage() == 0) {
            return true;
        }
        long lastPage = (long) Math.ceil((double) totalNumber / pageable.getSize());
        return pageable.getPage() < lastPage;
    }

    private static boolean matchPriceToRegex(BigDecimal price) {
        return String.valueOf(price.doubleValue()).matches(PRICE_REGEX);
    }

    public static boolean isDurationValid(int duration) {
        return Objects.nonNull(duration)
                ? isDurationRangeValid(duration) : duration == 0 || isDurationRangeValid(duration);
    }

    private static boolean isDurationRangeValid(int duration) {
        return duration >= MIN_PERIOD & duration <= MAX_PERIOD;
    }

    public static boolean isNameValid(String name) {
        return isStringFieldValid(name, NAME_REGEX);

    }

    public static boolean isDescriptionValid(String description) {
        return isStringFieldValid(description, DESCRIPTION_REGEX);
    }

    private static boolean isStringFieldValid(String field, String regex) {
        return Objects.isNull(field)
                ? isNotNullAndBlank(field) && field.matches(regex)
                : Objects.isNull(field) || (!field.isBlank() && field.matches(regex));
    }

    public static void checkGiftValidation(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto == null) {
            throw new SystemException(EMPTY_OBJECT);
        } else if (!isNameValid(giftCertificateDto.getName())) {
            throw new SystemException(CERTIFICATE_INVALID_NAME);
        } else if (!isDescriptionValid(giftCertificateDto.getDescription())) {
            throw new SystemException(CERTIFICATE_INVALID_DESCRIPTION);
        } else if (!isPriceValid(giftCertificateDto.getPrice())) {
            throw new SystemException(CERTIFICATE_INVALID_PRICE);
        } else if (!isDurationRangeValid(giftCertificateDto.getDuration())) {
            throw new SystemException(CERTIFICATE_INVALID_DURATION);
        } else if (!isTagListValid(giftCertificateDto.getTagDtoList())) {
            throw new SystemException(TAG_INVALID_NAME);
        }
    }

    public  void checkUserValidation(UserDto userDto){
        if (userDto == null) {
            throw new SystemException(EMPTY_OBJECT);
        } else if(!isNameValid(userDto.getName())){
            throw new SystemException(USER_INVALID_NAME);
        }
    }

    public  void checkPageableValidation(CustomPageable pageable,long totalOrderNumber){
        if (!isPageDataValid(pageable)) {
            throw new SystemException(INVALID_DATA_OF_PAGE);
        }
        if (!isPageExists(pageable, totalOrderNumber)) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }
    }

    public  void checkOrderValidation(RequestOrderDto order){
        /*if (!isPriceValid(order.getCertificateIdList().)) {
            throw new SystemException(CERTIFICATE_INVALID_PRICE);
        }*/
    }
}
