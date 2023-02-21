package com.example.homework2.Board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardRequestDto {

    private String title;

    private String content;


}
