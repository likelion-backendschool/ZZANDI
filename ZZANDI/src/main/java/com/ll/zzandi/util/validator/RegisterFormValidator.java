package com.ll.zzandi.util.validator;

import com.ll.zzandi.dto.UserDto;
import com.ll.zzandi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class RegisterFormValidator implements Validator {
  private final UserRepository userRepository;
  @Override
  public boolean supports(Class<?> aClass) {
    return  aClass.isAssignableFrom(UserDto.RegisterRequest.class);
  }

  @Override
  public void validate(Object target, Errors errors) {
    UserDto.RegisterRequest registerForm=(UserDto.RegisterRequest)target;
    if(userRepository.existsByUserEmail(registerForm.getUserEmail())){
      errors.rejectValue("userEmail","invalid.email", new Object[]{registerForm.getUserEmail()}, "이미 사용중인 이메일입니다.");
    }
    if(userRepository.existsByUserNickname(registerForm.getUserNickname())){
      errors.rejectValue("userNickname","invalid.nickname", new Object[]{registerForm.getUserEmail()}, "이미 사용중인 닉네임입니다.");
    }

  }
}