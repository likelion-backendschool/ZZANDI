package com.ll.zzandi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserApplicationException extends RuntimeException{

    private ErrorType errorType;
    private String message;

    public UserApplicationException(ErrorType errorType) {
        this.errorType = errorType;
        this.message = null;
    }


    @Override
    public String getMessage() {
        if (message == null) {
            return errorType.getMessage();
        }

        return String.format("%s. %s", errorType.getMessage(), message);
    }
}
