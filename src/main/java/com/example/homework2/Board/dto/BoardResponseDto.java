package com.example.homework2.Board.dto;

import com.example.homework2.Board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Long id;

    private String title;
    private String contents;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;

    @Builder
    public BoardResponseDto(Board entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.contents = entity.getContent();
        this.username = entity.getUser().getUsername();
        this.password = entity.getUser().getPassword();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
        this.commentList = entity.getCommentlist().stream().map(CommentResponseDto::comment_Service).toList();
    }

    public static BoardResponseDto User_Response(Board entity){
        return BoardResponseDto.builder()
                .entity(entity)
                .build();
    }
}
