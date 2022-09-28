package com.ll.zzandi.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final ErrorType errorType;
  private final String message;

  public CustomException(ErrorType errorType) {
    this.errorType = errorType;
    this.message = null;
  }

  @Override
  public String toString() {
    if (message == null) {
      return errorType.getMessage();
    }
    return String.format("%s. %s", errorType.getMessage(), message);
  }
}
