package com.cracker.user.dto;

import com.cracker.user.model.UserRole;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class UserRequestDto {
    private String email;
    private String pw;
    private String nickname;
    private String pic;
    private String marker_pic;
    private String role;
//    private UserRole role;
//    private String adminToken;
}
