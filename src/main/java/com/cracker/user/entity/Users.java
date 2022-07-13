package com.cracker.user.entity;

import com.cracker.auth.util.Timestamped;
import com.cracker.user.dto.JoinDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends Timestamped {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @JsonIgnore
    private String password;

    private String nickname;

    private String pic;

    private String marker_pic;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String status;

    @Column(unique = true)
    private String refreshToken;

    public Users(String email, String nickname, String pic, String marker_pic,
                 UserRole role) {
        this.email = email;
        this.nickname = nickname;
        this.pic = pic;
        this.marker_pic = marker_pic;
        this.role = role;
    }

    public Users(JoinDto requestJoinDTO) {
        this.email = requestJoinDTO.getEmail();
        this.nickname = requestJoinDTO.getNickname();
        this.pic = requestJoinDTO.getPic();
        this.marker_pic = requestJoinDTO.getMarker_pic();
        this.role = UserRole.valueOf(requestJoinDTO.getRole());
    }

    public void updateUserPassword(String password) {
        this.password = password;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
