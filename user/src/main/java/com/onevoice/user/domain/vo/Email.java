package com.onevoice.user.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class Email {

    @Column(name ="email",nullable = false)
    private String value;

    public Email(String value){
        validate(value);
        this.value = value;
    }

    public void validate(String value){
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        if (!value.matches(emailRegex)) {
            throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
        }
    }
}
