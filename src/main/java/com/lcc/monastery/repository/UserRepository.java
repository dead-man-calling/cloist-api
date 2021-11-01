package com.lcc.monastery.repository;

import com.lcc.monastery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumberAndUsername(String phoneNumber, String username);
}
