package com.cracker.cracker.user.repository;

import com.cracker.cracker.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByNickname(String nickname);

}