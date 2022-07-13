package com.cracker.userupdate.controller;


import com.cracker.userupdate.dto.UpdateUserRequestDto;
import com.cracker.userupdate.dto.UpdateUserResponseDto;
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

//    @GetMapping("/user")
//    //@ResponseBody // Restcontroller를 사용시 자동생성
//    public String userPage() {
//        return "user";
//    }

    @ResponseBody
    @PostMapping(value = "/user/update_profile/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public UpdateUserResponseDto UpdateProfile(@PathVariable long id, @ModelAttribute UpdateUserRequestDto updateUserRequestDto)
            throws IOException {

        UpdateUserResponseDto updateUserResponseDto = updateService.updateProfile(id, updateUserRequestDto);

        return updateUserResponseDto;
    }
}