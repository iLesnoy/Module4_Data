package com.epam.esm.gifts.impl;

import com.epam.esm.gifts.GiftCertificateService;
import com.epam.esm.gifts.converter.GiftCertificateAttributeConverter;
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
import com.epam.esm.gifts.model.Tag;
import com.epam.esm.gifts.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.gifts.exception.ExceptionCode.*;
import static org.springframework.data.domain.Sort.by;


@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateConverter giftCertificateConverter;
    private TagConverter tagConverter;
    private GiftCertificateAttributeConverter attributeConverter;
    private GiftCertificateRepository giftCertificateRepository;
    private TagServiceImpl tagService;
    private EntityValidator validator;
    private OrderRepository orderRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateConverter giftCertificateConverter, GiftCertificateAttributeConverter attributeConverter,
                                      GiftCertificateRepository giftCertificateRepository, TagServiceImpl tagService, EntityValidator validator,
                                      TagConverter tagConverter, OrderRepository orderRepository) {
        this.giftCertificateConverter = giftCertificateConverter;
        this.tagConverter = tagConverter;
        this.attributeConverter = attributeConverter;
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
        this.validator = validator;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        validator.checkGiftValidation(giftCertificateDto);
        GiftCertificate giftCertificate = giftCertificateConverter.dtoToGiftCertificate(giftCertificateDto);
        setTagListCertificate(giftCertificate);
        giftCertificateRepository.save(giftCertificate);
        return giftCertificateConverter.giftCertificateToDto(giftCertificate);
    }

    private void setTagListCertificate(GiftCertificate certificate) {
        certificate.setTagList(certificate.getTagList().stream().map(tagService::createTag)
                .collect(Collectors.toSet()));
    }

    @Override
    @Transactional
    public GiftCertificateDto update(Long id, GiftCertificateDto giftCertificateDto) {
        validator.checkGiftValidation(giftCertificateDto);
        GiftCertificate persistedCertificate = findCertificateById(id);

        setUpdatedFields(persistedCertificate, giftCertificateDto);
        setUpdatedTagList(persistedCertificate, giftCertificateDto.getTagDtoList());

        giftCertificateRepository.save(persistedCertificate);
        return giftCertificateConverter.giftCertificateToDto(persistedCertificate);
    }

    @Override
    public GiftCertificateDto findById(Long id) {
        return giftCertificateConverter.giftCertificateToDto(findCertificateById(id));
    }

    @Override
    public Page<GiftCertificateDto> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("command is not supported, please use searchByParameters");
    }

    @Override
    public GiftCertificate findCertificateById(Long id) {
        return giftCertificateRepository.findById(id).orElseThrow(() -> new SystemException((NON_EXISTENT_ENTITY)));
    }


    @Override
    @Transactional
    public Page<GiftCertificateDto> searchByParameters(GiftCertificateAttributeDto attributeDto, Pageable pageable) {
        if (!validator.isAttributeDtoValid(attributeDto)) {
            throw new SystemException(INVALID_ATTRIBUTE_LIST);
        }
        setDefaultParamsIfAbsent(attributeDto);
        Pageable sortedPageable = buildPageableSort(attributeDto.getSortingFieldList(), attributeDto.getOrderSort(), pageable);
        Page<GiftCertificate> certificatePage = Objects.nonNull(attributeDto.getTagNameList())
                ? giftCertificateRepository.findByAttributes(attributeDto.getTagNameList()
                , attributeDto.getTagNameList().size(), attributeDto.getSearchPart(), sortedPageable)
                : giftCertificateRepository.findByAttributes(attributeDto.getSearchPart(), sortedPageable);
        if (!validator.isPageExists(pageable, certificatePage.getTotalElements())) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new CustomPage<>(certificatePage.getContent(), certificatePage.getPageable(), certificatePage.getTotalElements())
                .map(giftCertificateConverter::giftCertificateToDto);
    }

    private void setDefaultParamsIfAbsent(GiftCertificateAttributeDto attributeDto) {
        if (Objects.isNull(attributeDto.getSearchPart())) {
            attributeDto.setSearchPart("");
        }
    }

    private Pageable buildPageableSort(List<String> sortingFieldList, String orderSort, Pageable pageable) {
        Sort.Direction direction = Objects.nonNull(orderSort)
                ? Sort.Direction.fromString(orderSort)
                : Sort.Direction.ASC;
        return CollectionUtils.isEmpty(sortingFieldList)
                ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "id"))
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()
                , by(direction, sortingFieldList.toArray(String[]::new)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        if(optionalGiftCertificate.isPresent()) {
            if (orderRepository.findFirstByCertificateListId(id).isPresent()) {
                throw new SystemException(USED_ENTITY);
            } else {
                giftCertificateRepository.delete(optionalGiftCertificate.get());
            }
        }else throw new SystemException(NON_EXISTENT_ENTITY);
        /*orderRepository.findFirstByCertificateListId(id).ifPresentOrElse(a->giftCertificateRepository.delete(optionalGiftCertificate.get()),
                ()->{throw new SystemException(NON_EXISTENT_ENTITY);});*/
    }

    private void setUpdatedFields(GiftCertificate persistedCertificate, GiftCertificateDto updatedCertificateDto) {
        String name = updatedCertificateDto.getName();
        String description = updatedCertificateDto.getDescription();
        BigDecimal price = updatedCertificateDto.getPrice();
        int duration = updatedCertificateDto.getDuration();
        if (Objects.nonNull(name) && !persistedCertificate.getName().equals(name)) {
            persistedCertificate.setName(name);
        }
        if (Objects.nonNull(description) && !persistedCertificate.getDescription().equals(description)) {
            persistedCertificate.setDescription(description);
        }
        if (Objects.nonNull(price) && !persistedCertificate.getPrice().equals(price)) {
            persistedCertificate.setPrice(price);
        }
        if (duration != 0 && persistedCertificate.getDuration() != duration) {
            persistedCertificate.setDuration(duration);
        }
    }


    private void setUpdatedTagList(GiftCertificate persistedCertificate, List<TagDto> tagDtoList) {
        if (CollectionUtils.isEmpty(tagDtoList)) {
            return;
        }
        Set<Tag> updatedTagSet = tagDtoList.stream()
                .map(tagConverter::dtoToTag)
                .map(tagService::createTag)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        persistedCertificate.setTagList(updatedTagSet);
    }
}
