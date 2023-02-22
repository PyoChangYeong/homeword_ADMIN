package com.example.homework2.Board.dto;

import com.example.homework2.Board.entity.Board;
import com.example.homework2.Board.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long id;
    private String comment;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto(Comment comentity) {
        this.id = comentity.getId();
        this.comment = comentity.getComment();
        this.createdAt = comentity.getBoard().getCreatedAt();
        this.modifiedAt = comentity.getBoard().getModifiedAt();
        this.username = comentity.getUser().getUsername();
    }

    public static CommentResponseDto comment_Service(Comment comment_entity){
        return CommentResponseDto.builder()
                .comentity(comment_entity)
                .build();
    }


}
