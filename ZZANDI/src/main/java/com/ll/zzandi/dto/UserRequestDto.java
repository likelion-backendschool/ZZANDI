package com.ll.zzandi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String userId;
    private String userPassword;
    private String userEmail;
    private String userNickname;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.userPassword=passwordEncoder.encode(this.userPassword);
    }
}
