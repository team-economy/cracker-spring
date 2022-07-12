package com.cracker.userupdate.controller;


import com.cracker.userupdate.dto.UpdateUserRequestDto;
import com.cracker.userupdate.dto.UpdateUserResponseDto;
import com.cracker.userupdate.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UpdateController {

    private final UpdateService updateService;

    @GetMapping("/user")
    //@ResponseBody // Restcontroller를 사용시 자동생성
    public String userPage() {
        return "user";
    }

    @PostMapping(value = "/user/update_profile/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public UpdateUserResponseDto UpdateProfile(@PathVariable long id, @RequestPart UpdateUserRequestDto updateUserRequestDto, @RequestPart MultipartFile file)
            throws IOException {

        UpdateUserResponseDto updateUserResponseDto = updateService.updateProfile(id, updateUserRequestDto, file);

        return updateUserResponseDto;
    }
}