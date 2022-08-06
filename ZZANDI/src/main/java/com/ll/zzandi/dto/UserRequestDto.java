package com.ll.zzandi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDto {

    private String userId;
    private String userPassword;
    private String userEmail;
    private String userNickname;
}
