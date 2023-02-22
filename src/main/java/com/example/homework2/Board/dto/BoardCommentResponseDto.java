package com.example.homework2.Board.dto;


import com.example.homework2.Board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardCommentResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;

    public BoardCommentResponseDto(Board board, List<CommentResponseDto> commentList) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = board.getUser().getUsername();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.commentList = commentList;
    }
}
