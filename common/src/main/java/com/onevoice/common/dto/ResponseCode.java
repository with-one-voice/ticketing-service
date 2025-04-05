package com.onevoice.common.dto;

import org.springframework.http.HttpStatus;

public enum ResponseCode {

    SUCCESS(HttpStatus.OK, "요청 성공"),
    CREATED(HttpStatus.CREATED,"생성 성공"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청"),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND,"데이터를 찾을 수 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN,"권한이 없습니다."),
    INTERNAL_SERVER_ERRROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");


    private final HttpStatus status;
    private final String message;

    ResponseCode(HttpStatus status,String message){
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus(){
        return status;
    }
    public String getMessage(){
        return message;
    }
}
