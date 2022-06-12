package com.epam.esm.gifts.impl;

import com.epam.esm.gifts.converter.OrderConverter;
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
    private ResponseOrderDto responseOrderDto;
    private GiftCertificate certificate;
    private UserDto userDto;
    private List<UserDto> userDtos;
    private List<ResponseOrderDto> orders;

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
    @Mock
    OrderConverter orderConverter;

    @BeforeEach
    void setUp() {
        orders = List.of(ResponseOrderDto.builder().id(1L).build());
        responseOrderDto = ResponseOrderDto.builder().build();
        certificate = GiftCertificate.builder().id(1L).name("certificate").build();
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
        userDtos = List.of(userDto, userDto);
        userPage = new CustomPage<>(List.of(user), pageable, 5L);
        orderPage = new CustomPage<>(List.of(order), pageable, 5L);
    }

    @Test
    void create() {
        doReturn(true).when(validator).isNameValid(anyString());
        doReturn(user).when(userConverter).dtoToUser(any(UserDto.class));
        doReturn(userDto).when(userConverter).userToDto(any(User.class));
        doReturn(user).when(userDao).save(any(User.class));
        UserDto actual = userService.create(userDto);
        assertEquals(userDto, actual);
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
    void findAll() {
        /*doReturn(userPage).when(userDao).findAll(pageable);
        doReturn(true).when(validator).isPageExists(any(Pageable.class), anyLong());
        doReturn(userDto).when(userConverter).userToDto(Mockito.any(User.class));
        Page<UserDto> actual = userService.findAll(pageable);
        assertEquals(pageable, actual);*/
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
    void findByName() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString());
        doReturn(userDto).when(userConverter).userToDto(any(User.class));
        doReturn(Optional.of(user)).when(userDao).findByName(Mockito.anyString());
        UserDto actual = userService.findByName("papa");
        assertEquals(userDto, actual);
    }

    @Test
    void findUserOrderList() {
        /*doReturn(true).when(userDao).existsById(anyLong());
        doReturn(orderPage).when(orderRepository).findOrderByUserId(Mockito.anyLong(), eq(pageable));
        doReturn(true).when(validator).isPageExists(any(Pageable.class), anyLong());

        doReturn(responseOrderDto).when(orderConverter).orderToDto(Mockito.any(Order.class));
        Page<ResponseOrderDto> actual = userService.findUserOrderList(1L, Pageable.ofSize(2));
        assertEquals(orders, actual);*/
    }

}