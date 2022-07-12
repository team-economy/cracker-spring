package com.cracker.place.service;

import com.cracker.place.domain.Place;
import com.cracker.place.dto.PlaceCreateRequestDto;
import com.cracker.place.dto.PlaceListRequestDto;
import com.cracker.place.repository.PlaceRepository;
import com.cracker.user.entity.Users;
import com.cracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(PlaceCreateRequestDto placeCreateRequestDto, String email) {
        Place place = Place.builder()
                .name(placeCreateRequestDto.getName())
                .addr(placeCreateRequestDto.getAddr())
                .addrRoad(placeCreateRequestDto.getAddrRoad())
                .coordX(placeCreateRequestDto.getCoordX())
                .coordY(placeCreateRequestDto.getCoordY())
                .phoneNum(placeCreateRequestDto.getPhoneNum())
                .cate(placeCreateRequestDto.getCate())
        .build();

        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("일치하는 메일이 없습니다")
        );
        place.registUser(user);

        return placeRepository.save(place).getId();
    }

    @Transactional
    public List<Place> placeList() {
        return placeRepository.findAll();
    }

    @Transactional
    public List<PlaceListRequestDto> placeListSearchByEmail(String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("일치하는 메일이 없습니다.")
        );
        List<PlaceListRequestDto> dtos = new ArrayList<PlaceListRequestDto>();
        List<Place> places = user.getPlaces();
        for(Place place : places){
            PlaceListRequestDto dto = PlaceListRequestDto.builder()
                    .id(place.getId())
                    .name(place.getName())
                    .addr(place.getAddr())
                    .addrRoad(place.getAddrRoad())
                    .coordX(place.getCoordX())
                    .coordY(place.getCoordY())
                    .phoneNum(place.getPhoneNum())
                    .cate(place.getCate())
            .build();
            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional
    public Long deletePlace(Long id) {
        placeRepository.deleteById(id);
        return id;
    }

    @Transactional
    public Place placeSearch(Long id){
        return placeRepository.getById(id);
    }
}
