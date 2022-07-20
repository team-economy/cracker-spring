package com.cracker.comment.controller;

import com.cracker.auth.security.UserPrincipal;
import com.cracker.auth.util.token.AuthTokenProvider;
import com.cracker.comment.dto.*;
import com.cracker.comment.entity.Comment;
import com.cracker.comment.repository.CommentRepository;
import com.cracker.comment.service.CommentService;
import com.cracker.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public void createComment(@RequestBody CommentCreateRequestDto commentCreateRequestDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        String email = userPrincipal.getEmail();
        commentService.save(commentCreateRequestDto, email);
    }

    @GetMapping("/comment")
    public List<CommentListResponseDto> getComment(@RequestParam("communityAddr")String communityAddr){
//        System.out.println(placeId);
        return commentService.commentList(communityAddr);
//        return commentRepository.findAllByOrderByModifiedAtDesc();
    }

    @DeleteMapping("/comment/{id}")
    public CommentDeleteResponseDto deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal){
        UserRole userRole = userPrincipal.getRole();
        CommentDeleteResponseDto commentDeleteResponseDto = new CommentDeleteResponseDto();

        if(userRole.equals(UserRole.ADMIN)) {
            commentService.deleteComment(id);
            commentDeleteResponseDto.setMsg("삭제 완료!! \n (관리자 계정)");
        } else {
            String email = userPrincipal.getEmail();
            long retId = commentService.deleteCommentByUserMail(id, email);
            if(retId == 0) {
                commentDeleteResponseDto.setMsg("본인이 아니라 삭제할 수 없습니다.");
            } else {
                commentDeleteResponseDto.setMsg("삭제 완료!!");
            }
        }

        return commentDeleteResponseDto;
    }

    @PutMapping("/comment/{id}")
    public CommentUpdateResponseDto updateComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto){
        String email = userPrincipal.getEmail();
        CommentUpdateResponseDto commentUpdateResponseDto = new CommentUpdateResponseDto();
        long retId = commentService.updateByUser(id, commentUpdateRequestDto, email);
        if(retId == 0) {
            commentUpdateResponseDto.setMsg("본인이 아니라 수정할 수 없습니다.");
        } else {
            commentUpdateResponseDto.setMsg("수정 완료!!");
        }
        return commentUpdateResponseDto;
    }

}
