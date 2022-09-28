package com.ll.zzandi.exception;

import lombok.Getter;

public class StudyException extends CustomException {

  public StudyException(ErrorType errorType) {
    super(errorType);
  }
}
