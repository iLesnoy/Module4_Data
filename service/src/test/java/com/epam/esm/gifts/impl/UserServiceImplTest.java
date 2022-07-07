package com.epam.esm.gifts.impl;

import com.epam.esm.gifts.converter.UserConverter;
import com.epam.esm.gifts.dao.OrderRepository;
import com.epam.esm.gifts.dao.UserRepository;
import com.epam.esm.gifts.dto.CustomPage;
import com.epam.esm.gifts.dto.ResponseOrderDto;
import com.epam.esm.gifts.dto.UserDto;
import com.epam.esm.gifts.exception.SystemException;
import com.epam.esm.gifts.model.GiftCertificate;
import com.epam.esm.gifts.model.Order;
import com.epam.esm.gifts.model.User;
import com.epam.esm.gifts.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private User user;
    private Order order;
    private UserDto userDto;

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    Page<Order> orderPage;
    @Mock
    Page<User> userPage;
    @Mock
    Pageable pageable;
    @Mock
    UserRepository userDao;
    @Mock
    OrderRepository orderRepository;
    @Mock
    UserConverter userConverter;
    @Mock
    EntityValidator validator;

    @BeforeEach
    void setUp() {
        order = Order.builder().id(1L)
                .purchaseTime((LocalDateTime.of(2001, 1, 1, 2, 3)))
                .cost(new BigDecimal("500"))
                .user(user)
                .certificateList(List.of(GiftCertificate.builder().build()))
                .build();
        user = User.builder().id(1L)
                .name("Slava").password("23312REWER")
                .orderList(List.of(order)).build();
        userDto = UserDto.builder().id(1L)
                .name("Slava").password("23123PERW").build();
        userPage = new CustomPage<>(List.of(user), pageable, 5L);
        orderPage = new CustomPage<>(List.of(order), pageable, 5L);
    }

    @Test
    void createThrowInvalidName() {
        doReturn(false).when(validator).isNameValid(anyString());
        SystemException exception = assertThrows(SystemException.class, () -> userService.create(userDto));
        assertEquals(40320, exception.getErrorCode());
    }



    @Test
    void updateThrowNonExist() {
        SystemException exception = assertThrows(SystemException.class, () -> userService.update(1L, userDto));
        assertEquals(40320, exception.getErrorCode());
    }

    @Test
    void findById() {
        doReturn(Optional.of(user)).when(userDao).findById(Mockito.anyLong());
        doReturn(userDto).when(userConverter).userToDto(Mockito.any(User.class));
        UserDto userDto = userService.findById(1L);
        assertEquals(userDto, userDto);
    }

    @Test
    void findByIdThrowNonExistEntity() {
        doReturn(Optional.empty()).when(userDao).findById(Mockito.anyLong());
        SystemException exception = assertThrows(SystemException.class, () -> userService.findById(1L));
        assertEquals(40410, exception.getErrorCode());
    }

    @Test
    void findAllInvalidPage() {
        doReturn(userPage).when(userDao).findAll(pageable);
        doReturn(false).when(validator).isPageExists(any(Pageable.class), anyLong());
        SystemException exception = assertThrows(SystemException.class, () -> userService.findAll(pageable));
        assertEquals(40051, exception.getErrorCode());
    }

    @Test
    void delete() {
        doReturn(Optional.of(user)).when(userDao).findById(anyLong());
        doReturn(true).when(orderRepository).existsOrderByUserId(Mockito.anyLong());
        doNothing().when(userDao).delete(any(User.class));
        userService.delete(1L);
        assertTrue(true);
    }

    @Test
    void deleteThrowNonExist() {
        doReturn(Optional.empty()).when(userDao).findById(anyLong());
        SystemException exception = assertThrows(SystemException.class, () -> userService.delete(1L));
        assertEquals(40410, exception.getErrorCode());
    }

    @Test
    void findUserById() {
        doReturn(userDto).when(userConverter).userToDto(any(User.class));
        doReturn(Optional.of(user)).when(userDao).findById(anyLong());
        UserDto actual = userService.findById(1L);
        assertEquals(userDto, actual);
    }

    @Test
    void findUserByNotExistId() {
        doReturn(Optional.empty()).when(userDao).findById(anyLong());
        SystemException exception = assertThrows(SystemException.class, () -> userService.findById(10000L));
        assertEquals(40410, exception.getErrorCode());
    }

    @Test
    void findByName() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString());
        doReturn(userDto).when(userConverter).userToDto(any(User.class));
        doReturn(Optional.of(user)).when(userDao).findByName(Mockito.anyString());
        UserDto actual = userService.findByName("papa");
        assertEquals(userDto, actual);
    }

    @Test
    void findByNotExistName() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString());
        doReturn(Optional.empty()).when(userDao).findByName(Mockito.anyString());
        SystemException exception = assertThrows(SystemException.class, () -> userService.findByName("name"));
        assertEquals(40410, exception.getErrorCode());
    }

}