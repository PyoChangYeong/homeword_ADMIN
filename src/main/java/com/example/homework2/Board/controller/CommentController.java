package com.example.homework2.Board.controller;

import com.example.homework2.Board.dto.*;
import com.example.homework2.Board.security.UserDetailsImpl;
import com.example.homework2.Board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Board")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{boardid}")
    public ResponseEntity<CommentResponseDto> create(@PathVariable Long boardid, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.create(boardid, requestDto, userDetails.getUser());
    }

    @PutMapping("/comment/{boardid}")
    public ResponseEntity<CommentResponseDto> updatePost(@PathVariable Long boardid, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.updatePost(boardid, userDetails.getUser(), commentRequestDto);
    }

    @DeleteMapping("/comment/{boardid}")
    public ResponseEntity<MegResponseDto> deletePost(@PathVariable Long boardid, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deletePost(boardid, userDetails.getUser());
    }

}
