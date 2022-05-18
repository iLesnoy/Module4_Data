package com.epam.esm.gifts;

import com.epam.esm.gifts.dto.ResponseOrderDto;
import com.epam.esm.gifts.dto.UserDto;
import com.epam.esm.gifts.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService extends BaseService<UserDto> {

    User findUserById(Long id);

    UserDto findByName(String name);

    Page<ResponseOrderDto> findUserOrderList(Long id, Pageable pageable);
}
