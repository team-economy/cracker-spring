package com.cracker.place.controller;

import com.cracker.auth.security.UserPrincipal;
import com.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.comment.service.CommentService;
import com.cracker.community.service.CommunityService;
import com.cracker.place.dto.*;
import com.cracker.place.entity.Place;
import com.cracker.place.service.PlaceService;
import com.cracker.user.entity.UserRole;
import com.cracker.user.entity.Users;
import lombok.RequiredArgsConstructor;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final AuthTokenProvider authTokenProvider;
    private final CommentService commentService;
    private final CommunityService communityService;

    @RequestMapping(value = "/places/create", method={RequestMethod.POST})
    public PlaceCreateResponseDto savePlace(@RequestBody PlaceCreateRequestDto placeCreateRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String email = userPrincipal.getEmail();
        Long retId = placeService.save(placeCreateRequestDto, email);

        PlaceCreateResponseDto placeCreateResponseDto = new PlaceCreateResponseDto();
        placeCreateResponseDto.setMsg("저장 완료!!");
        placeCreateResponseDto.setId(retId);

        return placeCreateResponseDto;
    }

    @GetMapping("/places")
    public List<PlaceListRequestDto> readPlace(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam(name = "userName", required = false)String userName) {
        System.out.println(userName);
        if(userName == null) {
            String email = userPrincipal.getEmail();

            return placeService.placeListSearch(email, true);
        }else {
            return placeService.placeListSearch(userName, false);
        }
    }


    /**
     * 맛집Id와 일치하는 맛집 삭제
     * @param id  Place id
     * @return // 삭제 완료 메세지
     */
    @DeleteMapping("/places/{id}")
    public PlaceDeleteResponseDto deletePlace(@PathVariable("id") Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserRole userRole = userPrincipal.getRole();
        Users user = placeService.findUserByPlaceId(id);
        long userId = user.getId();
        PlaceDeleteResponseDto placeDeleteResponseDto = new PlaceDeleteResponseDto();
        if(userRole.equals(UserRole.ADMIN)) {
            placeService.deletePlace(id, userId);
            placeDeleteResponseDto.setMsg("삭제 완료!! \n (관리자 계정)");
        } else {
            String email = userPrincipal.getEmail();
            long retId = placeService.deletePlaceByUserMail(id, email);
            if(retId == 0) {
                placeDeleteResponseDto.setMsg("본인이 아니라 삭제할 수 없습니다.");
            } else {
                placeDeleteResponseDto.setMsg("삭제 완료!!");
            }
        }

        return placeDeleteResponseDto;
    }

    @PostMapping("/places/count/{addr}")
    public PlaceCountDto countPlace(@PathVariable String addr) {
        return placeService.countPlace(addr);
    }
}
