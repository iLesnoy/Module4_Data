package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.UserService;
import com.epam.esm.gifts.dto.ResponseOrderDto;
import com.epam.esm.gifts.dto.UserDto;
import com.epam.esm.gifts.hateaos.HateoasBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final HateoasBuilder hateoasBuilder;

    @Autowired
    public UserController(UserService userService, HateoasBuilder hateoasBuilder) {
        this.userService = userService;
        this.hateoasBuilder = hateoasBuilder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('users:create')")
    public UserDto create(@RequestBody UserDto userDto) {
        UserDto created = userService.create(userDto);
        hateoasBuilder.setLinks(created);
        return created;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:read') || #id.equals(authentication.principal.userId)")
    public UserDto findById(@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        hateoasBuilder.setLinks(userDto);
        return userDto;
    }

    @GetMapping("/search/{name}")
    @PreAuthorize("hasAuthority('users:read') || #name.equals(authentication.principal.username)")
    public UserDto findByName(@PathVariable String name) {
        UserDto userDto = userService.findByName(name);
        hateoasBuilder.setLinks(userDto);
        return userDto;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public Page<UserDto> findAll(Pageable pageable) {
        Page<UserDto> page = userService.findAll(pageable);
        page.getContent().forEach(hateoasBuilder::setLinks);
        return page;
    }

    @GetMapping("/{id}/orders")
    @PreAuthorize("hasAuthority('users:read') || #id.equals(authentication.principal.userId)")
    public Page<ResponseOrderDto> findUserOrderList(@PathVariable Long id, Pageable pageable) {
        Page<ResponseOrderDto> page = userService.findUserOrderList(id, pageable);
        page.getContent().forEach(hateoasBuilder::setLinks);
        return page;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('users:delete')")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

}