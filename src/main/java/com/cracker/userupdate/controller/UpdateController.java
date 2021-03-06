package com.cracker.userupdate.controller;


import com.cracker.userupdate.dto.UpdateMarkerRequestDto;
import com.cracker.userupdate.dto.UpdateMarkerResponseDto;
import com.cracker.userupdate.dto.UpdateUserRequestDto;
import com.cracker.userupdate.dto.UpdateUserResponseDto;
import com.cracker.userupdate.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

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
    public UpdateUserResponseDto UpdateProfile(@PathVariable long id, @Validated @ModelAttribute UpdateUserRequestDto updateUserRequestDto, BindingResult result)
            throws IOException, NoSuchAlgorithmException {

        UpdateUserResponseDto updateUserResponseDto = updateService.updateProfile(id, updateUserRequestDto);

        return updateUserResponseDto;
    }

    @ResponseBody
    @PostMapping(value = "/user/update_marker/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public UpdateMarkerResponseDto UpdateMarker(@PathVariable long id, @Validated @ModelAttribute UpdateMarkerRequestDto updateMarkerRequestDto, BindingResult result)
            throws IOException, NoSuchAlgorithmException {

        UpdateMarkerResponseDto updateMarkerResponseDto = updateService.updateMarker(id, updateMarkerRequestDto);

        return updateMarkerResponseDto;
    }
}

