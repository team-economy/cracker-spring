package com.cracker.place.service;

import com.cracker.comment.entity.Comment;
import com.cracker.comment.repository.CommentRepository;
import com.cracker.community.dto.CommunityPlaceListDto;
import com.cracker.community.entity.Community;
import com.cracker.community.repository.CommunityRepository;
import com.cracker.place.dto.PlaceCountDto;
import com.cracker.place.dto.PlaceDeleteResponseDto;
import com.cracker.place.entity.Place;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;

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

        if (placeRepository.findByAddr(place.getAddr()) != null) {
            return null;
        }

        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("일치하는 메일이 없습니다")
        );
        place.registUser(user);

        Community savedCommunity = communityRepository.findByAddr(place.getAddr());

        if(savedCommunity == null){
            Community community = Community.builder()
                    .name(placeCreateRequestDto.getName())
                    .addr(placeCreateRequestDto.getAddr())
                    .addrRoad(placeCreateRequestDto.getAddrRoad())
                    .coordX(placeCreateRequestDto.getCoordX())
                    .coordY(placeCreateRequestDto.getCoordY())
                    .phoneNum(placeCreateRequestDto.getPhoneNum())
                    .cate(placeCreateRequestDto.getCate())
                    .markerPic(placeCreateRequestDto.getMarkerPic())
                    .url(placeCreateRequestDto.getUrl())
            .build();
            place.placeCommunity(community);
            communityRepository.save(community);
        }else{
            place.placeCommunity(savedCommunity);
        }
        placeRepository.save(place);
        return place.getCommunity().getId();
    }

    @Transactional
    public List<Place> placeList() {
        return placeRepository.findAll();
    }

    @Transactional
    public List<PlaceListRequestDto> placeListSearch(String emailOrUserName, boolean isEmail) {
        Users user;

        if(isEmail) {
            user = userRepository.findByEmail(emailOrUserName).orElseThrow(
                    () -> new IllegalArgumentException("일치하는 메일이 없습니다.")
            );
        }else {
            user = userRepository.findByNickname(emailOrUserName).orElseThrow(
                    () -> new IllegalArgumentException("일치하는 메일이 없습니다.")
            );
        }
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
                    .communityId(place.getCommunity().getId())
                    .markerPic(user.getMarker_pic())
            .build();
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 맛집 지우기
     */
    @Transactional
    public Long deletePlace(Long id, Long userId) {
        Place place = placeRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("일치하는 저장된 맛집이 없습니다.")
        );
        Community community = communityRepository.findByAddr(place.getAddr());
        Long communityId = place.getCommunity().getId();
        List<Comment> comments = commentRepository.findAll();
        if (comments.size() != 0) {
            for (Comment comment : comments) {
                Long commentUserId = comment.getUsers().getId();
                Long commentCommunityId = comment.getCommunity().getId();
                if (commentUserId.equals(userId) && commentCommunityId.equals(communityId)) {
                    Long commentId = comment.getId();
                    // place 삭제
                    commentRepository.deleteById(commentId);
                }
            }
        }
        placeRepository.deleteById(id);
        // 지운 place의 community에 존재하는 place가 없다면 community 삭제
        if (placeRepository.findByAddr(place.getAddr()) == null) {
            communityRepository.deleteById(communityId);
        }
        return id;
    }

    /**
     * 맛집 세기
     */
    @Transactional
    public PlaceCountDto countPlace(String addr) {
        List<Place> countPlaces = placeRepository.findByAddr(addr);
        int placeCount = countPlaces.size();
        PlaceCountDto dto = new PlaceCountDto();
        dto.setCount(placeCount);
        return dto;
    }

    /**
     * 유저 정보와 일치하는 맛집 지우기
     */
    @Transactional
    public long deletePlaceByUserMail(Long placeId, String userMail){
        // place를 통해 community와 community의 id 확인
        Place place = placeRepository.findById(placeId).orElseThrow(
                ()-> new NoSuchElementException("일치하는 저장된 맛집이 없습니다.")
        );
        Community community = communityRepository.findByAddr(place.getAddr());
        Long communityId = place.getCommunity().getId();
        List<Comment> comments = commentRepository.findAll();
        if (comments.size() != 0) {
            for (Comment comment : comments) {
                String commentEmail = comment.getUsers().getEmail();
                Long commentCommunityId = comment.getCommunity().getId();
                if (commentEmail.equals(userMail) && commentCommunityId.equals(communityId)) {
                    Long commentId = comment.getId();
                    // place 삭제
                    commentRepository.deleteById(commentId);
                }
            }
            if(place.getUsers().getEmail().equals(userMail)) {
                placeRepository.deleteById(placeId);
            } else {
                return 0;
            }
        } else {
            if(place.getUsers().getEmail().equals(userMail)) {
                placeRepository.deleteById(placeId);
            } else {
                return 0;
            }
        }

        // 지운 place의 community에 존재하는 place가 없다면 community 삭제
        if (placeRepository.findByAddr(place.getAddr()) == null) {
            communityRepository.deleteById(communityId);
        }
        return placeId;
    }

    @Transactional
    public Users findUserByPlaceId(Long id) {
        Place place = placeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당하는 장소가 없습니다.")
        );
        return place.getUsers();
    }

    @Transactional
    public Place placeSearch(Long id){
        return placeRepository.getById(id);
    }

    @Transactional
    public Long addPlace(Long communityId, String email){
        Community community = communityRepository.findById(communityId).orElseThrow(
                () -> new NoSuchElementException("일치하는 커뮤니티가 없습니다.")
        );

        PlaceCreateRequestDto placeCreateRequestDt = PlaceCreateRequestDto.builder()
                .name(community.getName())
                .addr(community.getAddr())
                .addrRoad(community.getAddrRoad())
                .coordX(community.getCoordX())
                .coordY(community.getCoordY())
                .phoneNum(community.getPhoneNum())
                .cate(community.getCate())
                .markerPic(community.getMarkerPic())
                .url(community.getUrl())
            .build();

        return save(placeCreateRequestDt, email);
    }
}
