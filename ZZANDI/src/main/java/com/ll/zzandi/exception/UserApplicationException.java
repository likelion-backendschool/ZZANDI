package com.ll.zzandi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserApplicationException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public UserApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }


    @Override
    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        }

        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
