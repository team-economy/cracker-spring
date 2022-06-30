package com.cracker.controller;

import com.cracker.domain.Place;
import com.cracker.dto.PlaceCreateRequestDto;
import com.cracker.dto.PlaceCreateResponseDto;
import com.cracker.dto.PlaceListRequestDto;
import com.cracker.service.PlaceService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @RequestMapping(value = "/places/create", method={RequestMethod.POST})
    public PlaceCreateResponseDto savePlace(@RequestBody PlaceCreateRequestDto placeCreateRequestDto) {
        Long retId = placeService.save(placeCreateRequestDto);

        PlaceCreateResponseDto placeCreateResponseDto = new PlaceCreateResponseDto();
        placeCreateResponseDto.setMsg("저장 완료!!");

        return placeCreateResponseDto;
    }

    @GetMapping("/places/{id}")
    public PlaceListRequestDto readPlace(@PathVariable("id") Long id) {
        PlaceListRequestDto responsePlaceDto = placeService.findPlace(id);
        return placeService.findPlace(id);
    }

    @DeleteMapping("/places/{id}")
    public String deletePlace(@PathVariable("id") Long id) {
        long retId = placeService.deletePlace(id);
        return "delete";
    }
}
