package com.ll.zzandi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {
    DUPLICATED_USER_NAME("404", "User name is duplicated"),
    NOT_FOUND("404", "페이지를 찾을수 없습니다."),
    INTERNAL_SERVER_ERROR("500", "서버에 문제가 있습니다."),
    ;


    private String errorCode;
    private String message;
}
