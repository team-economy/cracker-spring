package com.cracker.place.controller;

import com.cracker.place.domain.Place;
import com.cracker.place.dto.PlaceCreateRequestDto;
import com.cracker.place.dto.PlaceCreateResponseDto;
import com.cracker.place.dto.PlaceDeleteResponseDto;
import com.cracker.place.service.PlaceService;
import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/places")
    public List<Place> readPlace() {
        List<Place> places = placeService.placeList();
        return placeService.placeList();
    }

    @DeleteMapping("/places/{id}")
    public PlaceDeleteResponseDto deletePlace(@PathVariable("id") Long id) {
        long retId = placeService.deletePlace(id);
        PlaceDeleteResponseDto placeDeleteResponseDto = new PlaceDeleteResponseDto();
        placeDeleteResponseDto.setMsg("삭제 완료!!");

        return placeDeleteResponseDto;
    }
}
