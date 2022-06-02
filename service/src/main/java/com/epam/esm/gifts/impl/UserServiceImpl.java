package com.epam.esm.gifts.impl;

import com.epam.esm.gifts.UserService;
import com.epam.esm.gifts.converter.OrderConverter;
import com.epam.esm.gifts.converter.UserConverter;
import com.epam.esm.gifts.dao.OrderRepository;
import com.epam.esm.gifts.dao.UserRepository;
import com.epam.esm.gifts.dto.CustomPage;
import com.epam.esm.gifts.dto.ResponseOrderDto;
import com.epam.esm.gifts.dto.UserDto;
import com.epam.esm.gifts.exception.SystemException;
import com.epam.esm.gifts.model.Order;
import com.epam.esm.gifts.model.User;
import com.epam.esm.gifts.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import static com.epam.esm.gifts.dto.UserDto.Role.USER;
import static com.epam.esm.gifts.exception.ExceptionCode.*;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;
    private EntityValidator validation;
    private UserConverter userConverter;
    private OrderConverter orderConverter;
    private OrderRepository orderRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EntityValidator validation,
                           UserConverter userConverter, OrderConverter orderConverter,
                           PasswordEncoder passwordEncoder, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.validation = validation;
        this.userConverter = userConverter;
        this.orderConverter = orderConverter;
        this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        checkUserValidation(userDto);
        setCreatedUserParams(userDto);
        User user = userConverter.dtoToUser(userDto);
        return userConverter.userToDto(userRepository.save(user));
    }

    private void setCreatedUserParams(UserDto userDto) {
        userDto.setRole(USER);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }

    private void checkUserValidation(UserDto userDto) {
        if (!validation.isNameValid(userDto.getName())) {
            throw new SystemException(USER_INVALID_NAME);
        }
        if (!validation.isPasswordValid(userDto.getPassword())) {
            throw new SystemException(USER_INVALID_PASSWORD);
        }
        if (userRepository.findByName(userDto.getName()).isPresent()) {
            throw new SystemException(DUPLICATE_NAME);
        }
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        checkUserValidation(userDto);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            validation.checkUserValidation(userDto);
            userRepository.save(userConverter.dtoToUser(userDto));
            return userDto;
        }
        throw new SystemException(NON_EXISTENT_ENTITY);
    }


    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        if (!validation.isPageExists(pageable, userPage.getTotalElements())) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new CustomPage<>(userPage.getContent(), userPage.getPageable(), userPage.getTotalElements())
                .map(userConverter::userToDto);
    }


    @Override
    public void delete(Long id) {
        User user = findUserById(id);
        if (orderRepository.existsOrderByUserId(user.getId())) {
            userRepository.delete(user);
        } else {
            throw new SystemException(USED_ENTITY);
        }
    }

    @Override
    public UserDto findById(Long id) {
        return userConverter.userToDto(findUserById(id));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    public UserDto findByName(String name) {
        if (!validation.isNameValid(name)) {
            throw new SystemException(USER_INVALID_NAME);
        }
        return userRepository.findByName(name).map(userConverter::userToDto)
                .orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    public Page<ResponseOrderDto> findUserOrderList(Long id, Pageable pageable) {
        if (!userRepository.existsById(id)) {
            throw new SystemException(NON_EXISTENT_ENTITY);
        }
        Page<Order> orderPage = orderRepository.findOrderByUserId(id, pageable);
        if (!validation.isPageExists(pageable, orderPage.getTotalElements())) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new CustomPage<>(orderPage.getContent(), orderPage.getPageable(), orderPage.getTotalElements())
                .map(orderConverter::orderToDto);
    }
}
