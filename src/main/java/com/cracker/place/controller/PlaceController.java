package com.cracker.place.controller;

import com.cracker.auth.security.UserPrincipal;
import com.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.place.domain.Place;
import com.cracker.place.dto.PlaceCreateRequestDto;
import com.cracker.place.dto.PlaceCreateResponseDto;
import com.cracker.place.dto.PlaceDeleteResponseDto;
import com.cracker.place.dto.PlaceListRequestDto;
import com.cracker.place.service.PlaceService;
import lombok.RequiredArgsConstructor;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final AuthTokenProvider authTokenProvider;

    @RequestMapping(value = "/places/create", method={RequestMethod.POST})
    public PlaceCreateResponseDto savePlace(@RequestBody PlaceCreateRequestDto placeCreateRequestDto, @CookieValue(required = false, name = "refresh_token") String token) {
        String email = authTokenProvider.getEmailByToken(token);
        Long retId = placeService.save(placeCreateRequestDto, email);


        PlaceCreateResponseDto placeCreateResponseDto = new PlaceCreateResponseDto();
        placeCreateResponseDto.setMsg("저장 완료!!");

        return placeCreateResponseDto;
    }

    @GetMapping("/places")
    public List<PlaceListRequestDto> readPlace(@CookieValue(required = false, name = "refresh_token") String token) {
        String email = authTokenProvider.getEmailByToken(token);
//        List<PlaceListRequestDto> places = placeService.placeList(email);
        return placeService.placeListSearchByEmail(email);
    }

    @DeleteMapping("/places/{id}")
    public PlaceDeleteResponseDto deletePlace(@PathVariable("id") Long id) {
        long retId = placeService.deletePlace(id);
        PlaceDeleteResponseDto placeDeleteResponseDto = new PlaceDeleteResponseDto();
        placeDeleteResponseDto.setMsg("삭제 완료!!");

        return placeDeleteResponseDto;
    }
}
