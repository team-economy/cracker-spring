package com.cracker.community.controller;

import com.cracker.community.dto.CommunityPlaceListDto;
import com.cracker.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping("/places/all")
    public List<CommunityPlaceListDto> readAllPlace() {
        return communityService.allPlaceList();
    }
}