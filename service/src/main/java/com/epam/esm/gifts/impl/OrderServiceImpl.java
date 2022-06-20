package com.epam.esm.gifts.impl;

import com.epam.esm.gifts.OrderService;
import com.epam.esm.gifts.converter.OrderConverter;
import com.epam.esm.gifts.dao.OrderRepository;
import com.epam.esm.gifts.dto.CustomPage;
import com.epam.esm.gifts.dto.RequestOrderDto;
import com.epam.esm.gifts.dto.ResponseOrderDto;
import com.epam.esm.gifts.exception.SystemException;
import com.epam.esm.gifts.model.GiftCertificate;
import com.epam.esm.gifts.model.Order;
import com.epam.esm.gifts.model.User;
import com.epam.esm.gifts.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.gifts.exception.ExceptionCode.*;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private EntityValidator validator;
    private UserServiceImpl userService;
    private GiftCertificateServiceImpl giftCertificateService;
    private OrderConverter orderConverter;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, EntityValidator validator, UserServiceImpl userService,
                            GiftCertificateServiceImpl giftCertificateService, OrderConverter orderConverter) {
        this.orderRepository = orderRepository;
        this.validator = validator;
        this.userService = userService;
        this.giftCertificateService = giftCertificateService;
        this.orderConverter = orderConverter;
    }

    @Override
    public ResponseOrderDto create(ResponseOrderDto responseOrderDto) {
        throw new UnsupportedOperationException("command is not supported in OrderServiceImpl class ");
    }

    @Override
    @Transactional
    public ResponseOrderDto create(RequestOrderDto orderDto) {
        if(!validator.isRequestOrderDataValid(orderDto)){
            throw new SystemException(INVALID_ATTRIBUTE_LIST);
        }
        User user = userService.findUserById(orderDto.getUserId());
        List<GiftCertificate> giftCertificates = orderDto.getCertificateIdList()
                .stream().map(giftCertificateService::findCertificateById).collect(Collectors.toList());
        Order order = Order.builder().user(user).certificateList(giftCertificates).build();
        return orderConverter.orderToDto(orderRepository.save(order));
    }

    @Override
    public ResponseOrderDto update(Long id, ResponseOrderDto orderDto) {
        throw new UnsupportedOperationException("update method is not supported in OrderServiceImpl class");
    }

    @Override
    public ResponseOrderDto findById(Long id) {
        return orderConverter.orderToDto(findOrderById(id));
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    @Transactional
    public Page<ResponseOrderDto> findAll(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        if(!validator.isPageExists(pageable,orderPage.getTotalElements())){
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new CustomPage<>(orderPage.getContent(), orderPage.getPageable(), orderPage.getTotalElements())
                .map(orderConverter::orderToDto);
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("delete method is not supported in OrderServiceImpl class");
    }
}
