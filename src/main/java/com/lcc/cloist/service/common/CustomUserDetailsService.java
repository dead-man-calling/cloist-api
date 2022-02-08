package com.lcc.cloist.service.common;

import com.lcc.cloist.advice.exception.UserNotFoundException;
import com.lcc.cloist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String userPk) {
        return userRepository.findById(Long.valueOf(userPk)).orElseThrow(UserNotFoundException::new);
    }
}
