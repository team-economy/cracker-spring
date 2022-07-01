package com.cracker.user.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String pw;
    private String name;
    private String pic;
    private String marker_pic;
    private String status;
    private String roles; // USER, ADMIN

    public Users(String email, String pw, String name, String pic, String marker_pic, String roles) {
        this.email = email;
        this.pw = pw;
        this.name = name;
        this.pic = pic;
        this.marker_pic = marker_pic;
        this.roles = roles;
    }

    // role에 대한 것
    // enum으로 안하고 ,로 구분해서 ROLE을 입력 후 파싱
    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
