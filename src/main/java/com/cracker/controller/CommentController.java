package com.cracker.controller;

import com.cracker.domain.Comment;
import com.cracker.dto.CommentCreateRequestDto;
import com.cracker.dto.CommentUpdateRequestDto;
import com.cracker.repository.CommentRepository;
import com.cracker.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController // JSON으로 데이터를 주고받음을 선언
@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @PostMapping("/comment")
    public void createComment(@RequestBody CommentCreateRequestDto commentCreateRequestDto){
        long redId= commentService.save(commentCreateRequestDto);
    }

    @GetMapping("/comment")
    public List<Comment> getComment(){
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        return commentRepository.findAllByModifiedAtBetweenOrderByModifiedAtDesc(start,end);
    }

    @DeleteMapping("/comment/{id}")
    public void deleteComment(@PathVariable Long id){

        long retId = commentService.delete(id);
    }

    @PutMapping("/comment/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto){
        long retId = commentService.update(id, commentUpdateRequestDto);
    }

}
