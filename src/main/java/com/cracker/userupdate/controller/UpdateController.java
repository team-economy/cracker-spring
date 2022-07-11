package com.cracker.userupdate.controller;


import com.cracker.user.entity.Users;
import com.cracker.userupdate.dto.UpdateUserRequestDto;
import com.cracker.userupdate.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UpdateController {

    private final UpdateService updateService;

    @GetMapping("/user")
    //@ResponseBody // Restcontroller를 사용시 자동생성
    public String userPage(){
        return "user";
    }

    @PutMapping(value = "/user/update_profile/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Users UpdateProfile (@PathVariable long id, @RequestPart UpdateUserRequestDto updateUserRequestDto, @RequestPart(required = false) MultipartFile file)
    throws IOException {

        log.info("nickname : {}, status: {}, pic: {}", updateUserRequestDto.getNickname(), updateUserRequestDto.getStatusMessage(), file);
        Users users = updateService.updateProfile(id, updateUserRequestDto, file);
        return users;
    }

//    @PostMapping(value = "/user/update_profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}) // 현재 유저를 확인하는 코드 추가
//    public Users UpdateProfile (@RequestPart = false) String nickname,
//        @RequestPart (name="pic", required = false) MultipartFile multipartFile,
//        (@RequestPart = false) String status,
//        throws IOException {
//        return UserService.updateProfile(nickname, multipartFile, status);
//    }

//    @RequestMapping(value="/user/update_profile", method= RequestMethod.POST)
//    public UpdateUserResponseDto saveUserPic(@RequestPart UpdateUserRequestDto updateUserRequestDto) throws SQLException, Exception {
//        UpdateService.update(updateUserRequestDto);
//
//        UpdateUserResponseDto updateUserResponseDto = new UpdateUserResponseDto();
//        updateUserResponseDto.setMsg("수정 완료!");
//
//        return UpdateUserResponseDto;
//    }
}

