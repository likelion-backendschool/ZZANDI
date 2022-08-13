package com.ll.zzandi.dto;


import com.ll.zzandi.domain.User;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RegisterRequest{
        @NotBlank(message = "")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9,_-]{5,20}$", message = "5~20자의 영문 소문자, 숫자와 특수기호 -,_만 사용가능합니다.")
        private String userId;
        @NotBlank
        private String userPassword;
        @Email
        @NotBlank
        private String userEmail;

        @NotBlank
        private String userNickname;

        public void encodePassword(PasswordEncoder passwordEncoder) {
            this.userPassword = passwordEncoder.encode(this.userPassword);
        }
    }

    @Getter
    @Setter
    public static class RegisterResponse {
        private String userId;
        private String userEmail;
        private String userNickname;
        private boolean userEmailVerified;

        @Builder
        public RegisterResponse(User user){
            this.userId=user.getUserId();
            this.userEmail=user.getUserEmail();
            this.userNickname=user.getUserNickname();
            this.userEmailVerified=user.isUserEmailVerified();
        }
    }
}
