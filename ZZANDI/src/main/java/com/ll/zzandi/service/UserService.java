package com.ll.zzandi.service;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.UserDto;
import com.ll.zzandi.exception.ErrorCode;
import com.ll.zzandi.exception.UserApplicationException;
import com.ll.zzandi.repository.UserRepository;
import com.ll.zzandi.util.mail.EmailMessage;
import com.ll.zzandi.util.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;

    @Transactional
    public User join(final UserDto.RegisterRequest registerRequest) {
        userRepository.findByUserId(registerRequest.getUserId()).ifPresent(x -> {
            throw new UserApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", registerRequest.getUserId()));
        });
        registerRequest.encodePassword(passwordEncoder);
        User newUser=User.of(registerRequest);
        newUser.generateEmailCheckToken();
        System.out.println(newUser.toString());
        sendSignUpConfirmEmail(newUser);
        return userRepository.save(newUser);
    }

    public void sendSignUpConfirmEmail(User user) {
        Context context = new Context();
        context.setVariable("link", "/check-email-token?token=" + user.getEmailCheckToken() +
                "&email=" + user.getUserEmail());
        context.setVariable("nickname", user.getUserNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "ZZANDI 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host", "http://localhost:8080");
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(user.getUserEmail())
                .subject("ZZANDI, 회원 가입 인증")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }
}


