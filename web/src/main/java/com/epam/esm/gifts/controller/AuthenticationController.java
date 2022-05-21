package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.UserService;
import com.epam.esm.gifts.dto.AuthenticationRequestDto;
import com.epam.esm.gifts.dto.AuthenticationResponseDto;
import com.epam.esm.gifts.dto.UserDto;
import com.epam.esm.gifts.hateaos.HateoasBuilder;
import com.epam.esm.gifts.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final HateoasBuilder hateoasBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(UserService userService, HateoasBuilder hateoasBuilder, JwtTokenProvider jwtTokenProvider
            , AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.hateoasBuilder = hateoasBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto signUp(@RequestBody UserDto userDto) {
        UserDto created = userService.create(userDto);
        hateoasBuilder.setLinks(created);
        return created;
    }

    @PostMapping("/login")
    public AuthenticationResponseDto authenticate(@RequestBody AuthenticationRequestDto requestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getName(), requestDto.getPassword()));
        UserDto userDto = userService.findByName(requestDto.getName());
        String token = jwtTokenProvider.createToken(userDto.getName(), userDto.getRole().name());
        return AuthenticationResponseDto.builder().username(requestDto.getName()).token(token).build();
    }

    @PostMapping("/login/oauth")
    public AuthenticationResponseDto oauthAuthenticate(@PathVariable AuthenticationRequestDto requestDto) {
        return AuthenticationResponseDto.builder().username(requestDto.getName()).build();
    }
}
