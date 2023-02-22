package com.example.homework2.Board.entity;

import com.example.homework2.Board.dto.CommentRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends Timestamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "users_id",nullable = false)
    private User user;

    @Builder
    public Comment(CommentRequestDto requestDto, Board board, User user) {
        this.comment = requestDto.getComment();
        this.board = board;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto,User user){
        this.comment = requestDto.getComment();
        this.user = user;
    }

    public static Comment Comment_Service(CommentRequestDto requestDto,Board board,User user){
        return Comment.builder()
                .requestDto(requestDto)
                .board(board)
                .user(user)
                .build();
    }


}
