package com.onevoice.common.exception;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonResponse<Object>> handleBaseException(BaseException ex) {
        log.warn("[BaseException] {}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return CommonResponse.of(ex.getResponseCode(), null);
    }

    //  @Valid 유효성 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("[ValidationException] {}", errorMessage);
        return CommonResponse.of(ResponseCode.BAD_REQUEST.getStatus(), null, errorMessage);
    }

    // 제약조건 위반 예외
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("[ConstraintViolationException] {}", ex.getMessage());
        return CommonResponse.of(ResponseCode.BAD_REQUEST.getStatus(), null, ex.getMessage());
    }

    // 예상치 못한 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Object>> handleException(Exception ex) {
        log.error("[UnhandledException] ", ex);
        return CommonResponse.of(ResponseCode.INTERNAL_SERVER_ERRROR.getStatus(), null, ex.getMessage());
    }
}
