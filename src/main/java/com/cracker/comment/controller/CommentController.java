package com.cracker.comment.controller;

import com.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.comment.domain.Comment;
import com.cracker.comment.dto.CommentCreateRequestDto;
import com.cracker.comment.dto.CommentListRequestDto;
import com.cracker.comment.dto.CommentListResponseDto;
import com.cracker.comment.dto.CommentUpdateRequestDto;
import com.cracker.comment.repository.CommentRepository;
import com.cracker.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController // JSON으로 데이터를 주고받음을 선언
@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성
public class CommentController {

    Date date = new Date();

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final AuthTokenProvider authTokenProvider;

    @PostMapping("/comment")
    public void createComment(@RequestBody CommentCreateRequestDto commentCreateRequestDto, @CookieValue(required = false, name = "refresh_token") String token){
        String email = authTokenProvider.getEmailByToken(token);
        long redId= commentService.save(commentCreateRequestDto, email);
    }

    @GetMapping("/comment")
    public List<Comment> getComment(@RequestParam("placeId")Long placeId){
//        System.out.println(placeId);
//        return commentService.commentList(placeId);
        return commentRepository.findAllByOrderByModifiedAtDesc();
    }

    @DeleteMapping("/comment/{id}")
    public void deleteComment(@PathVariable Long id){

        long retId = commentService.delete(id);
    }

    @PutMapping("/comment/{id}")
    public long updateComment(@PathVariable Long id, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto){
        Comment comment = commentService.update(id, commentUpdateRequestDto);
        return comment.getId();
    }

}
