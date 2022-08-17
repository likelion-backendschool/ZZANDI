package com.ll.zzandi.dto;


import com.ll.zzandi.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDto {
    @Getter
    @Setter
    public static class RegisterRequest{
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9]{5,10}$", message = "회원 아이디(ID)는 띄어쓰기 없이 5~20자리의 영문자와 숫자 조합만 가능합니다.")
        private String userId;
        @NotBlank
        @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!;@#$%^&*()+|=])[A-Za-z\\d~!;@#$%^&*()+|=]{8,16}$", message = "띄어쓰기 없이 영문, 숫자, 특수문자를 각각 포함하여 8~16자 사이로 입력해주세요")
        private String userPassword;
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank
        private String userEmail;

        @NotBlank
        private String userNickname;

        private ArrayList<String> interests=new ArrayList<>();

        public RegisterRequest(@NotBlank @Pattern(regexp = "^[a-zA-Z0-9]{5,10}$", message = "회원 아이디(ID)는 띄어쓰기 없이 5~20자리의 영문자와 숫자 조합만 가능합니다.") String userId, @NotBlank @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!;@#$%^&*()+|=])[A-Za-z\\d~!;@#$%^&*()+|=]{8,16}$", message = "띄어쓰기 없이 영문, 숫자, 특수문자를 각각 포함하여 8~16자 사이로 입력해주세요") String userPassword, @Email(message = "올바른 이메일 형식이 아닙니다.") @NotBlank String userEmail, @NotBlank String userNickname) {
            this.userId = userId;
            this.userPassword = userPassword;
            this.userEmail = userEmail;
            this.userNickname = userNickname;
        }

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