package com.onevoice.user.domain;

import com.onevoice.common.security.UserRole;
import com.onevoice.user.domain.vo.Email;
import com.onevoice.user.domain.vo.Password;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@NoArgsConstructor
@Table(name ="p_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="user_id")
    private UUID id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String email,String encodedPassword, UserRole role){
        this.email = new Email(email);
        this.password = new Password(encodedPassword);
        this.role = role;
    }

    public static User createUser(String email, String rawPassword, UserRole role,
        PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        return new User(email, encodedPassword, role);
    }

}
