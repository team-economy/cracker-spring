package com.cracker.user.dto;

import lombok.*;
import org.springframework.core.io.ClassPathResource;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDto {

    ClassPathResource resource = new ClassPathResource("static/profile_placeholder.png");

    private String email;
    private String pw;
    private String name;
    private String pic = String.valueOf(resource);
    private String marker_pic = "classpath:/";
    private String role;
    private String adminToken;

    @Builder
    public SignUpRequestDto(String email,
                            String pw,
                            String name,
                            String pic,
                            String marker_pic,
                            String role) {
        this.email = email;
        this.name = name;
        this.pw = pw;
        this.pic = pic;
        this.marker_pic = marker_pic;
        this.role = role;
    }
}