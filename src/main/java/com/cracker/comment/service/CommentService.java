package com.cracker.comment.service;

import com.cracker.comment.domain.Comment;
import com.cracker.comment.dto.CommentCreateRequestDto;
import com.cracker.comment.dto.CommentListRequestDto;
import com.cracker.comment.dto.CommentListResponseDto;
import com.cracker.comment.dto.CommentUpdateRequestDto;
import com.cracker.comment.repository.CommentRepository;
import com.cracker.place.domain.Place;
import com.cracker.place.repository.PlaceRepository;
import com.cracker.user.entity.Users;
import com.cracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service //실제 로직을 처리하는 부분
@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
public class CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    //comment 작성
    @Transactional
    public long save(CommentCreateRequestDto commentCreateRequestDto, String email){
        Comment comment = Comment.builder()
                .comment(commentCreateRequestDto.getComment())
                .build();


        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("일치하는 메일이 없습니다.")
        );
        Place place = placeRepository.findById(commentCreateRequestDto.getPlaceId()).orElseThrow(
                () -> new NoSuchElementException("일치하는 맛집이 없습니다.")
        );

        comment.placeUserComment(place, user);

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public List<Comment> commentList(Long placeId){
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new NoSuchElementException("일치하는 맛집이 없습니다")
        );

//        List<CommentListResponseDto> dtos = new ArrayList<CommentListResponseDto>();
        List<Comment> comments = place.getComments();
//        for(Comment comment : comments){
//            System.out.println("======================================");
//            CommentListResponseDto dto = CommentListResponseDto.builder()
//                    .userNickname(comment.getUsers().getNickname())
//                    .userEmail(comment.getUsers().getEmail())
////                    .comment(comment.getComment())
//                    .modifiedAt(comment.getModifiedAt())
//            .build();
//            dtos.add(dto);
//        }
        return comments;
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
}
