package com.cracker.community.controller;

import com.cracker.auth.security.UserPrincipal;
import com.cracker.auth.service.AuthService;
import com.cracker.community.dto.CommunityPlaceListDto;
import com.cracker.community.service.CommunityService;
import com.cracker.place.dto.PlaceListRequestDto;
import com.cracker.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
    private final AuthService authService;

    @GetMapping("/places/all")
    public List<CommunityPlaceListDto> readAllPlace(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userMail;
        if (userPrincipal == null) {
            userMail = null;
        } else {
            userMail = userPrincipal.getEmail();
            Users user = authService.findUserByEmail(userMail);
            if (user == null) {
                userMail = null;
            }
        }
        return communityService.allPlaceList(userMail);
    }
}
