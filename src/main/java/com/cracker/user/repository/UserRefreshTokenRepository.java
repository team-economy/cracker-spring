package com.cracker.user.repository;

import com.cracker.user.model.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {

    Optional<UserRefreshToken> findRefreshTokenByIdx(long refreshIdx);

    void deleteByEmail(String email);

    Optional<UserRefreshToken> findByEmail(String email);

}
