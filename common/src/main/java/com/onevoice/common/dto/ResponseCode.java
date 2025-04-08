package com.onevoice.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    /**
     * ────────────────────────────────────────────
     * @Response Code Convention
     * ───────────────────────────────────────
     * @Format: @ABCD
     *  @- A : HTTP 상태 범주 (4 = 4xx Client Error, 5 = 5xx Server Error 등)
     *  @- B : 도메인 구분 (1 = User, 5 = Ticket, ...)
     *  @- CD: 세부 에러 코드 (40 = Not Found, 90 = Conflict 등)
     * ───────────────────────────────────────
     * @User Domain (B = 1)
     * ───────────────────────────────────────
     * @4140 USER_NOT_FOUND        → 404 Not Found
     * @4190 DUPLICATE_USER        → 409 Conflict
     * ───────────────────────────────────────
     * @Ticket Domain (B = 5)
     * ───────────────────────────────────────
     * @4590 TICKET_ALREADY_BOOKED → 409 Conflict
     * @※ 참고: 에러 메시지와 매핑된 HTTP 상태 코드도 함께 기재하면 더 명확합니다.
     */



    // 2xx: 성공
    SUCCESS(2000, HttpStatus.OK, "요청 성공"),
    CREATED(2010, HttpStatus.CREATED, "생성 성공"),

    // 4xx: 클라이언트 오류
    BAD_REQUEST(4000, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(4010, HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(4030, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND(4040, HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),

    // 5xx: 서버 오류
    INTERNAL_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // User
    USER_NOT_FOUND(4140, HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다."),
    DUPLICATE_USER(4190, HttpStatus.CONFLICT, "이미 존재하는 사용자 입니다."),
    PASSWORD_NOT_MATCH(4100, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // Payment
    PAYMENT_NOT_FOUND(4740, HttpStatus.NOT_FOUND, "존재하지 않는 결제 정보입니다."),

    // Notification
    NOTIFICATION_NOT_FOUND(4840, HttpStatus.NOT_FOUND, "존재하지 않는 알림 정보입니다."),

    // Venue (8004)
    VENUE_NOT_FOUND(4440, HttpStatus.NOT_FOUND, "존재하지 않는 공연장 입니다."),
    DUPLICATE_VENUE(4490, HttpStatus.CONFLICT, "이미 존재하는 공연장 입니다."),

    //Seat (B = 6)
    SEAT_NOT_FOUND(4640, HttpStatus.NOT_FOUND, "존재하지 않는 좌석입니다."),
    SEAT_ALREADY_HELD(4690, HttpStatus.CONFLICT, "이미 선점된 좌석입니다.");

    private final int code; // 커스텀 코드 (우리 마음대로 정하는 거)
    private final HttpStatus status;
    private final String message;

    ResponseCode(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
