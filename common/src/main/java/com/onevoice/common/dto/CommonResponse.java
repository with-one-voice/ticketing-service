package com.onevoice.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@Getter
public class CommonResponse<T>{

    private final T result;
    private final String message;

    /**
     * 성공 요청 생성
     * @param data
     * @return
     * @param <T>
     */
    public static <T>ResponseEntity<CommonResponse<T>> success(T data){
        return ResponseEntity.ok(createCommonResponse(data, ResponseCode.SUCCESS.getMessage()));
    }

    /**
     * 공통 응답 생성
     * @param status
     * @param data
     * @param message
     * @return
     * @param <T>
     */
    public static <T>ResponseEntity<CommonResponse<T>> of(HttpStatus status,T data,String message){
        return new ResponseEntity<>(createCommonResponse(data, message), status);
    }

    /**
     * 공통 응답 생성
     * @param responseCode
     * @param data
     * @return
     * @param <T>
     */
    public static <T>ResponseEntity<CommonResponse<T>> of(ResponseCode responseCode,T data){
        return new ResponseEntity<>(createCommonResponse(data, responseCode.getMessage()),
            responseCode.getStatus());
    }

    private static <T> CommonResponse<T> createCommonResponse(T data,String message){
        return new CommonResponse<>(data,message);
    }
}
