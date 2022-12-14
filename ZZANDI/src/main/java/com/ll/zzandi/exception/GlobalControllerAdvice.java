package com.ll.zzandi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler({StudyException.class, TeamMateException.class})
  public String applicationHandler(CustomException e, Model model) {
    log.error("Error occurs {}", e.toString());
    model.addAttribute("ErrorCode",e.getErrorType().getErrorCode());
    model.addAttribute("message",e.getErrorType().getMessage());
    return "error/globalErrorPage";
  }

  @ExceptionHandler(UserApplicationException.class)
  public String applicationHandler(UserApplicationException e, Model model) {
    log.error("Error occurs {}", e.toString());
    model.addAttribute("ErrorCode",e.getErrorType().getErrorCode());
    model.addAttribute("message",e.getErrorType().getMessage());
    return "error/globalErrorPage";
  }

  @ExceptionHandler(RuntimeException.class)
  public String applicationHandler(RuntimeException e,Model model) {
    log.error("Error occurs {}", e.toString());

    model.addAttribute("ErrorCode",ErrorType.INTERNAL_SERVER_ERROR.getErrorCode());
    model.addAttribute("message",ErrorType.INTERNAL_SERVER_ERROR.getMessage());
    return "error/globalErrorPage";
  }


}
