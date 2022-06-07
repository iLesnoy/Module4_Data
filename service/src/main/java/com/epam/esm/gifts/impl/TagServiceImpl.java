package com.epam.esm.gifts.impl;

import com.epam.esm.gifts.TagService;
import com.epam.esm.gifts.converter.TagConverter;
import com.epam.esm.gifts.dao.GiftCertificateRepository;
import com.epam.esm.gifts.dao.TagRepository;
import com.epam.esm.gifts.dto.CustomPage;
import com.epam.esm.gifts.dto.TagDto;
import com.epam.esm.gifts.exception.SystemException;
import com.epam.esm.gifts.model.GiftCertificate;
import com.epam.esm.gifts.model.Tag;
import com.epam.esm.gifts.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.gifts.exception.ExceptionCode.*;

@Service
public class TagServiceImpl implements TagService {

    TagRepository tagRepository;
    TagConverter tagConverter;
    EntityValidator entityValidator;
    GiftCertificateRepository giftCertificateRepository;


    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagConverter tagConverter,
                          EntityValidator entityValidator,GiftCertificateRepository giftCertificateRepository) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
        this.entityValidator = entityValidator;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) {
        Tag tag = createTag(tagConverter.dtoToTag(tagDto));
        return tagConverter.tagToDto(tag);
    }

    public Tag createTag(Tag tag) {
        if (entityValidator.isNameValid(tag.getName())) {
            return tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag));
        }
        throw new SystemException(TAG_INVALID_NAME);
    }

    @Override
    public TagDto update(Long id, TagDto tagDto) {
        Optional<Tag> optionalUser = tagRepository.findById(id);
        if (optionalUser.isPresent()) {
            if (!entityValidator.isNameValid(tagDto.getName())) {
                throw new SystemException(TAG_INVALID_NAME);
            }
            tagRepository.save(tagConverter.dtoToTag(tagDto));
        }
        throw new SystemException(NON_EXISTENT_ENTITY);
    }

    @Override
    @Transactional
    public Page<TagDto> findAll(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(pageable);
        if (!entityValidator.isPageExists(pageable, tagPage.getTotalElements())) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new PageImpl<>(tagPage.getContent(), tagPage.getPageable(), tagPage.getTotalElements())
                .map(tagConverter::tagToDto);
    }

    @Override
    public TagDto findById(Long id) {
        return tagConverter.tagToDto(findTagById(id));
    }

    private Tag findTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (giftCertificateRepository.findFirstByTagList_Id(id).isPresent()) {
            throw new SystemException(USED_ENTITY);
        }
        tagRepository.delete(optionalTag.get());
    }

}
