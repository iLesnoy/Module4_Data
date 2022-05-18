package com.epam.esm.gifts;

import com.epam.esm.gifts.dto.GiftCertificateAttributeDto;
import com.epam.esm.gifts.dto.GiftCertificateDto;
import com.epam.esm.gifts.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftCertificateService extends BaseService<GiftCertificateDto> {

    Page<GiftCertificateDto> searchByParameters(GiftCertificateAttributeDto attributeDto, Pageable pageable);

    GiftCertificate findCertificateById(Long id);

}
