package com.onevoice.user.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Slf4j
public class Password {

    @Column(name = "password", nullable = false)
    private String value;

    public Password(String encodedValue){
        this.value = encodedValue;
    }

    public Password of(String rawPassword, PasswordEncoder passwordEncoder) {
        validate(rawPassword);
        return new Password(passwordEncoder.encode(rawPassword));
    }

    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        log.info("비밀번호 비교 : {}", rawPassword);
        log.info("인코딩된 비밀번호 : {}", this.value);
        return encoder.matches(rawPassword, this.value);
    }

    private  void validate(String value){
        if (value.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 합니다.");
        }
    }

    public Password update(String rawPassword,PasswordEncoder passwordEncoder){
        return of(rawPassword, passwordEncoder);
    }

}
