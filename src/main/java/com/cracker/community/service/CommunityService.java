package com.cracker.community.service;

import com.cracker.community.dto.CommunityPlaceListDto;
import com.cracker.community.entity.Community;
import com.cracker.community.repository.CommunityRepository;
import com.cracker.place.domain.Place;
import com.cracker.place.dto.PlaceListRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    @Transactional
    public Community communitySearch(Long id){
        return communityRepository.getById(id);
    }

    @Transactional
    public List<CommunityPlaceListDto> allPlaceList() {
        List<CommunityPlaceListDto> dtos = new ArrayList<>();
        List<Community> communities = communityRepository.findAll();
        for(Community community : communities){
            CommunityPlaceListDto dto = CommunityPlaceListDto.builder()
                    .id(community.getId())
                    .name(community.getName())
                    .addr(community.getAddr())
                    .addrRoad(community.getAddrRoad())
                    .phoneNum(community.getPhoneNum())
                    .cate(community.getCate())
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }
}
