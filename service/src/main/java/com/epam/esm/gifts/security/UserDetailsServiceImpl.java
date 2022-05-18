package com.epam.esm.gifts.security;

import com.epam.esm.gifts.dao.UserRepository;
import com.epam.esm.gifts.exception.SystemException;
import com.epam.esm.gifts.model.User;
import com.epam.esm.gifts.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.epam.esm.gifts.exception.ExceptionCode.INVALID_CREDENTIALS;
import static com.epam.esm.gifts.exception.ExceptionCode.USER_INVALID_NAME;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final EntityValidator validator;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, EntityValidator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!validator.isNameValid(username)) {
            throw new SystemException(USER_INVALID_NAME);
        }
        User user = userRepository.findByName(username).orElseThrow(() -> new SystemException(INVALID_CREDENTIALS));
        return SecurityUserDetailsBuilder.create(user);
    }

}
