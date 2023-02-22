package com.example.homework2.Board.entity;

import com.example.homework2.Board.dto.BoardRequestDto;
import com.example.homework2.Board.dto.CommentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;
    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> commentlist = new ArrayList<>();



    @Builder
    public Board(BoardRequestDto boardRequestDto, User user, CommentRequestDto commentRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.user = user;
    }

    public void update(BoardRequestDto requestDto,User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
