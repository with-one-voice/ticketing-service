package com.onevoice.common.dto;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class CommonResponse<T> {
    private final int code;
    private final String message;
    private final T result;

    public static <T> ResponseEntity<CommonResponse<T>> success(T result){
        return ResponseEntity.ok(createCommonResponse(ResponseCode.SUCCESS, result));
    }

    public static <T> ResponseEntity<CommonResponse<T>> of(ResponseCode code,T result){
        return new ResponseEntity<>(createCommonResponse(code, result), code.getStatus());
    }

    public static <T> CommonResponse<T> createCommonResponse(ResponseCode code, T result) {
        return new CommonResponse<>(code, result);
    }

    public CommonResponse(ResponseCode code, T result) {
        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = result;
    }
}
