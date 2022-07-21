package com.cracker.comment.service;

import com.cracker.comment.dto.CommentCreateRequestDto;
import com.cracker.comment.dto.CommentListResponseDto;
import com.cracker.comment.dto.CommentUpdateRequestDto;
import com.cracker.comment.entity.Comment;
import com.cracker.comment.repository.CommentRepository;
import com.cracker.community.entity.Community;
import com.cracker.community.repository.CommunityRepository;
import com.cracker.place.dto.AdminCommentListRequestDto;
import com.cracker.place.repository.PlaceRepository;
import com.cracker.user.entity.Users;
import com.cracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.*;

@Service //실제 로직을 처리하는 부분
@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
public class CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    private final CommunityRepository communityRepository;

    //comment 작성
    @Transactional
    public Long save(CommentCreateRequestDto commentCreateRequestDto, String email){
        Comment comment = Comment.builder()
                .comment(commentCreateRequestDto.getComment())
                .build();


        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("일치하는 메일이 없습니다.")
        );

        Community community = communityRepository.findByAddr(commentCreateRequestDto.getCommunityAddr());

        comment.communityComment(community);
        comment.UserComment(user);

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public List<CommentListResponseDto> commentList(String communityAddr){

        Community community = communityRepository.findByAddr(communityAddr);

        List<CommentListResponseDto> dtos = new ArrayList<CommentListResponseDto>();

        List<Comment> comments = community.getComments();
        for(Comment comment : comments){
            CommentListResponseDto dto = CommentListResponseDto.builder()
                    .id(comment.getId())
                    .userNickname(comment.getUsers().getNickname())
                    .userEmail(comment.getUsers().getEmail())
                    .comment(comment.getComment())
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .userProfileImg(comment.getUsers().getPic())
                    .userId(comment.getUsers().getId())
            .build();
            dtos.add(dto);
        }

        Collections.sort(dtos, new CompareCreatedAtDesc());

        return dtos;
    }

    //수정 시간별 내림차순 정리
    static class CompareCreatedAtDesc implements Comparator<CommentListResponseDto>{
        @Override
        public int compare(CommentListResponseDto o1, CommentListResponseDto o2){
            return o2.getCreatedAt().compareTo(o1.getCreatedAt());
        }
    }

    // comment를 지움
    @Transactional
    public Long delete(@PathVariable Long id){
        commentRepository.deleteById(id);
        return id;
    }
    // comment 업데이트
    @Transactional
    public Comment update(Long id, CommentUpdateRequestDto commentUpdateRequestDto){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("사용자가 아닙니다.")
        );
        comment.updateComment(commentUpdateRequestDto);
        return comment;
    }

    @Transactional
    public List<AdminCommentListRequestDto> adminCommentList() {
        List<Comment> comments = commentRepository.findAll();

        List<AdminCommentListRequestDto> dtos = new ArrayList<AdminCommentListRequestDto>();
        for (Comment comment : comments) {
            AdminCommentListRequestDto dto = AdminCommentListRequestDto.builder()
                    .id(comment.getId())
                    .userNickname(comment.getUsers().getNickname())
                    .userEmail(comment.getUsers().getEmail())
                    .comment(comment.getComment())
                    .modifiedAt(comment.getModifiedAt())
                    .build();
            dtos.add(dto);
        }
        Collections.sort(dtos, new AdminCompareModifiedDesc());
        return dtos;
    }

    static class AdminCompareModifiedDesc implements Comparator<AdminCommentListRequestDto>{
        @Override
        public int compare(AdminCommentListRequestDto o1, AdminCommentListRequestDto o2){
            return o2.getModifiedAt().compareTo(o1.getModifiedAt());
        }
    }

    @Transactional
    public Long adminDeleteComment(Long id) {
        commentRepository.deleteById(id);
        return id;
    }
}



