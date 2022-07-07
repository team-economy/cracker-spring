package com.cracker.cracker.user.entity;

import com.cracker.cracker.auth.util.Timestamped;
import com.cracker.cracker.user.dto.JoinDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends Timestamped {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    @Column(nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID uid;

    @Column(nullable = false, unique = true, length = 30)
    private String loginId;

    @JsonIgnore
    private String loginPassword;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(unique = true)
    private String refreshToken;

    public Users(JoinDto requestJoinDTO, UUID uid) {
        this.loginId = requestJoinDTO.getEmail();
        this.nickName = requestJoinDTO.getNickName();
        this.role = RoleType.valueOf(requestJoinDTO.getRole());
        this.uid = uid;
    }

    public void updateUserPassword(String password) {
        this.loginPassword = password;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
