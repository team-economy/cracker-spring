package com.cracker.place.controller;

import com.cracker.place.domain.Place;
import com.cracker.place.dto.PlaceDeleteResponseDto;
import com.cracker.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final PlaceService placeService;

    @GetMapping("/admin/places")
    public List<Place> readPlace() {
        List<Place> places = placeService.placeList();
        return placeService.placeList();
    }


    @DeleteMapping("/admin/places/{id}")
    public PlaceDeleteResponseDto deletePlace(@PathVariable("id") Long id) {
        long retId = placeService.deletePlace(id);
        PlaceDeleteResponseDto placeDeleteResponseDto = new PlaceDeleteResponseDto();
        placeDeleteResponseDto.setMsg("삭제 완료!!");

        return placeDeleteResponseDto;
    }
}
