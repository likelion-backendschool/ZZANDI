package com.ll.zzandi.exception;

import com.ll.zzandi.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(UserApplicationException.class)
  public ResponseEntity<?> applicationHandler(UserApplicationException e) {
    log.error("Error occurs {}", e.toString());
    return ResponseEntity.status(e.getErrorCode().getStatus())
        .body(Response.error(e.getErrorCode().name()));
  }

  // 임시로 일단 만들어둔 팀원 예외
  @ExceptionHandler(TeamMateException.class)
  public ResponseEntity<?> exceptionhandling(RuntimeException e) {
    String message = e.getMessage();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }
}
