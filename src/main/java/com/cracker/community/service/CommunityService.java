package com.cracker.community.service;

import com.cracker.comment.dto.CommentListResponseDto;
import com.cracker.community.dto.CommunityPlaceListDto;
import com.cracker.community.entity.Community;
import com.cracker.community.repository.CommunityRepository;
import com.cracker.place.entity.Place;
import com.cracker.place.repository.PlaceRepository;
import com.cracker.user.entity.Users;
import com.cracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Community communitySearch(Long id){
        return communityRepository.getById(id);
    }

    @Transactional
    public List<CommunityPlaceListDto> allPlaceList(String userMail) {
        if (userMail == null) {
            List<CommunityPlaceListDto> dtos = new ArrayList<CommunityPlaceListDto>();
            List<Community> communities = communityRepository.findAll();
            for (Community community : communities) {
                CommunityPlaceListDto dto = CommunityPlaceListDto.builder()
                        .communityId(community.getId())
                        .name(community.getName())
                        .addr(community.getAddr())
                        .addrRoad(community.getAddrRoad())
                        .coordX(community.getCoordX())
                        .coordY(community.getCoordY())
                        .phoneNum(community.getPhoneNum())
                        .cate(community.getCate())
                        .markerPic("/marker_pics/marker-default.png")
                        .url(community.getUrl())
                        .countPlaces(community.getCount())
                        .build();
                dtos.add(dto);
            }
            Collections.sort(dtos, new ComparedCountDesc());
            return dtos;
        } else {
            Users user = userRepository.findByEmail(userMail).orElseThrow(
                    () -> new IllegalArgumentException("일치하는 메일이 없습니다.")
            );

            List<CommunityPlaceListDto> dtos = new ArrayList<CommunityPlaceListDto>();
            List<Community> communities = communityRepository.findAll();
            for (Community community : communities) {
                String marker_pic = "/marker_pics/marker-default.png";
                List<Place> places = community.getPlaces();
                for (Place place : places) {
                    if (place.getUsers().getEmail().equals(userMail)) {
                        marker_pic = user.getMarker_pic();
                        break;
                    }
                }

                CommunityPlaceListDto dto = CommunityPlaceListDto.builder()
                        .communityId(community.getId())
                        .name(community.getName())
                        .addr(community.getAddr())
                        .addrRoad(community.getAddrRoad())
                        .coordX(community.getCoordX())
                        .coordY(community.getCoordY())
                        .phoneNum(community.getPhoneNum())
                        .cate(community.getCate())
                        .markerPic(marker_pic)
                        .url(community.getUrl())
                        .countPlaces(community.getCount())
                        .build();
                dtos.add(dto);
            }
            Collections.sort(dtos, new ComparedCountDesc());
            return dtos;
        }
    }

    static class ComparedCountDesc implements Comparator<CommunityPlaceListDto> {
        @Override
        public int compare(CommunityPlaceListDto o1, CommunityPlaceListDto o2){
            return o2.getCountPlaces().compareTo(o1.getCountPlaces());
        }
    }
}
