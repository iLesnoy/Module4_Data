package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.GiftCertificateService;
import com.epam.esm.gifts.dto.GiftCertificateAttributeDto;
import com.epam.esm.gifts.dto.GiftCertificateDto;
import com.epam.esm.gifts.hateaos.HateoasBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api("Controller GiftCertificateController crud operations")
@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final HateoasBuilder hateoasBuilder;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, HateoasBuilder hateoasBuilder) {
        this.giftCertificateService = giftCertificateService;
        this.hateoasBuilder = hateoasBuilder;
    }

    @ApiOperation(value = "GiftCertificateDto", notes = "use giftCertificateDto")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('certificates:create')")
    public GiftCertificateDto create(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto created = giftCertificateService.create(giftCertificateDto);
        hateoasBuilder.setLinks(giftCertificateDto);
        return created;
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('certificates:update')")
    public GiftCertificateDto update(@PathVariable Long id,
                                     @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto updated = giftCertificateService.update(id, giftCertificateDto);
        hateoasBuilder.setLinks(updated);
        return updated;
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable Long id) {
        GiftCertificateDto certificateDto = giftCertificateService.findById(id);
        hateoasBuilder.setLinks(certificateDto);
        return certificateDto;
    }

    @GetMapping
    public Page<GiftCertificateDto> findByAttributes(GiftCertificateAttributeDto attribute, Pageable pageable) {
        Page<GiftCertificateDto> page = giftCertificateService.searchByParameters(attribute, pageable);
        page.getContent().forEach(hateoasBuilder::setLinks);
        return page;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('certificates:delete')")
    public void deleteById(@PathVariable Long id) {
        giftCertificateService.delete(id);
    }
}
