package com.ll.zzandi.config.security;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
UserDetailsService - DB의 사용자 정보를 가져오는 인터페이스
 */
@Service
@Getter
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /*
    회원의 정보를 Spring Security가 인식할 수 있는 객체(UserDetails)로 변환
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정입니다"));

        List<GrantedAuthority> authorities = new ArrayList<>();  // 권한 저장
        authorities.add(new SimpleGrantedAuthority(String.valueOf(user.getUserId())));

        return new UserContext(user, authorities);
    }
}
