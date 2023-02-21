package com.example.homework2.Board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class MegResponseDto {
    private String msg;
    private int Code;

    @Builder
    public MegResponseDto(String msg, int code) {
        this.msg = msg;
        this.Code = code;
    }
    //  HttpStatus 상태 입력으로 Dto 만들기
    public static MegResponseDto User_ServiceCode(HttpStatus status, String msg){
        return MegResponseDto.builder()
                .code(status.value())
                .msg(msg)
                .build();
    }

//    code입력으로 Dto 만들기
    public static MegResponseDto jwt_filter(int code, String msg){
        return MegResponseDto.builder()
                .code(code)
                .msg(msg)
                .build();
    }



}
