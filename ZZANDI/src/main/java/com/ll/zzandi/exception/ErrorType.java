package com.ll.zzandi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {
    DUPLICATED_USER_NAME("404", "User name is duplicated"),
    NOT_FOUND("404", "페이지를 찾을수 없습니다."),
    INTERNAL_SERVER_ERROR("500", "내부 서버에 문제가 있습니다."),

    DUPLICATED_TEAMMATE("409", "이미 신청한 스터디입니다."),
    FULL_STUDY("409", "팀원이 모두 모집되어 신청할 수 없습니다."),

    NOT_LEADER("403", "수정할 권한이 없습니다.")
    ;


    private String errorCode;
    private String message;
}
