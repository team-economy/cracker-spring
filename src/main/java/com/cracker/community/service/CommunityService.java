package com.cracker.community.service;

import com.cracker.community.dto.CommunityPlaceListDto;
import com.cracker.community.entity.Community;
import com.cracker.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        List<CommunityPlaceListDto> dtos = new ArrayList<CommunityPlaceListDto>();
        List<Community> communities = communityRepository.findAll();
        for(Community community : communities){
            CommunityPlaceListDto dto = CommunityPlaceListDto.builder()
                    .communityId(community.getId())
                    .name(community.getName())
                    .addr(community.getAddr())
                    .addrRoad(community.getAddrRoad())
                    .coordX(community.getCoordX())
                    .coordY(community.getCoordY())
                    .phoneNum(community.getPhoneNum())
                    .cate(community.getCate())
                    .markerPic("static/marker_pics/marker-default.png")
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }
}
