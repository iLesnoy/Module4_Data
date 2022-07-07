package com.epam.esm.gifts.converter;

import com.epam.esm.gifts.dto.UserDto;
import com.epam.esm.gifts.model.Role;
import com.epam.esm.gifts.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User dtoToUser(UserDto userDto){
        return User.builder().id(userDto.getId())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .role(Role.valueOf(userDto.getRole().name())).build();
    }

    public UserDto userToDto(User user){
        return UserDto.builder().id(user.getId())
                .name(user.getName()).password(user.getPassword())
                .role(UserDto.Role.valueOf(user.getRole().name())).build();
    }
}
