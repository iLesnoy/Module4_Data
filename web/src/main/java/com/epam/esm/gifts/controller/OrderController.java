package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.dto.RequestOrderDto;
import com.epam.esm.gifts.dto.ResponseOrderDto;
import com.epam.esm.gifts.hateaos.HateoasBuilder;
import com.epam.esm.gifts.impl.OrderServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api("controller with Crud Order operations")
@RestController
@RequestMapping("/api/orders")
public class OrderController {


    OrderServiceImpl orderService;
    HateoasBuilder hateoasBuilder;

    @Autowired
    public OrderController(OrderServiceImpl orderService, HateoasBuilder hateoasBuilder) {
        this.orderService = orderService;
        this.hateoasBuilder = hateoasBuilder;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('orders:read')")
    public Page<ResponseOrderDto> findAll(Pageable pageable){
        Page<ResponseOrderDto> page = orderService.findAll(pageable);
        page.getContent().forEach(hateoasBuilder::setLinks);
        return page;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('orders:read')")
    public ResponseOrderDto findById(@PathVariable Long id){
        ResponseOrderDto orderDto = orderService.findById(id);
        hateoasBuilder.setLinks(orderDto);
        return orderDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('orders:create')")
    public ResponseOrderDto create(@RequestBody RequestOrderDto orderDto){
        ResponseOrderDto responseOrderDto = orderService.create(orderDto);
        hateoasBuilder.setLinks(responseOrderDto);
        return responseOrderDto;
    }
}
