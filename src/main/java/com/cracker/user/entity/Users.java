package com.cracker.user.entity;

import com.cracker.comment.entity.Comment;
import com.cracker.auth.util.Timestamped;

import com.cracker.userupdate.dto.UpdateMarkerRequestDto;
import com.cracker.userupdate.dto.UpdateUserRequestDto;
import com.cracker.place.entity.Place;

import com.cracker.user.dto.JoinDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends Timestamped {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @JsonIgnore
    private String password;

    private String nickname;

    private String pic;

    private String marker_pic;

    private String statusMessage;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String status;

    @Column(unique = true)
    private String refreshToken;

    @OneToMany(mappedBy = "users", fetch = LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "users", fetch = LAZY)
    private List<Place> places = new ArrayList<>();

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

    public void updateUserProfile(UpdateUserRequestDto updateUserRequestDto, String filepath){
        this.nickname = updateUserRequestDto.getNickname();
        this.pic = filepath;
        this.statusMessage = updateUserRequestDto.getStatusMessage();
    }

    public void updateUserMarker(String markerpath){
        this.marker_pic = markerpath;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
