package com.example.homework2.Board.exception;

import com.example.homework2.Board.entity.ErrorCode.ErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private final ErrorCode errorcode;

    public ApiException(ErrorCode Errorcode){
        this.errorcode = getErrorcode();
    }
}
