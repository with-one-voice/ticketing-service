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

    /**
     * 커스텀 예외 처리 (BaseException 상속 구조)
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonResponse<Void>> handleBaseException(BaseException ex) {
        ResponseCode code = ex.getResponseCode();
        log.warn("[BaseException] {} - {}", code.getCode(), code.getMessage(), ex);
        return ResponseEntity
            .status(code.getStatus())
            .body(CommonResponse.createCommonResponse(code, null));
    }

    /**
     * @Valid or @Validated 바인딩 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("잘못된 요청입니다.");
        log.warn("[Validation Error] {}", errorMessage);
        return ResponseEntity
            .status(ResponseCode.BAD_REQUEST.getStatus())
            .body(CommonResponse.createCommonResponse(ResponseCode.BAD_REQUEST, errorMessage));
    }

    /**
     * 단일 파라미터 @Validated 실패
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
            .stream()
            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
            .findFirst()
            .orElse("잘못된 요청입니다.");
        log.warn("[ConstraintViolation] {}", errorMessage);
        return ResponseEntity
            .status(ResponseCode.BAD_REQUEST.getStatus())
            .body(CommonResponse.createCommonResponse(ResponseCode.BAD_REQUEST, errorMessage));
    }

    /**
     * 그 외 모든 예외
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleUnexpectedException(Exception ex) {
        log.error("[Unhandled Exception]", ex);
        return ResponseEntity
            .status(ResponseCode.INTERNAL_ERROR.getStatus())
            .body(CommonResponse.createCommonResponse(ResponseCode.INTERNAL_ERROR, null));
    }
}
