package com.cracker.user.model;


import com.cracker.user.dto.UserRequestDto;
import com.cracker.user.utils.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class Users extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String email;

    @Column(nullable = false, length = 100)
    private String pw;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, length = 20)
    private String pic;

    @Column(nullable = false, length = 20)
    private String marker_pic;

    @Column
    private String status;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    public Users(String email, String pw, String nickname, String pic, String marker_pic, UserRole role) {
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.pic = pic;
        this.marker_pic = marker_pic;
        this.role = role;
    }

    public Users(UserRequestDto requestDto) {
        this.email = requestDto.getEmail();
        this.pw = requestDto.getPw();
        this.nickname = requestDto.getNickname();
        this.pic = requestDto.getPic();
        this.marker_pic = requestDto.getMarker_pic();
        this.role = requestDto.getRole();
    }
}
