package com.ll.zzandi.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
UserDetailsService
DB의 데이터는 암호화되어 있으므로, 실제 인증을 담당하는 AuthenticationProvider 커스터마이징
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
    authentication에 저장된 아이디, 비밀번호로 인증
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = authentication.getName();
        String userPassword = (String) authentication.getCredentials();

        UserContext userContext = (UserContext) userDetailsService.loadUserByUsername(userId);

        if (!passwordEncoder.matches(userPassword, userContext.getUser().getUserPassword())) {
            throw new BadCredentialsException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }

        // 토큰 발급
        return new UsernamePasswordAuthenticationToken(userContext.getUser(), null, userContext.getAuthorities());
    }

    /*
    인증 객체가 토큰인 경우, true
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
