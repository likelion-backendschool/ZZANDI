package com.ll.zzandi.config.security;

import com.ll.zzandi.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/*
userdetails.User - UserDetails(사용자 정보를 담는 인터페이스) 구현체
 */
@Getter
public class UserContext extends org.springframework.security.core.userdetails.User {

    private final User user;

    public UserContext(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUserId(), user.getUserPassword(), authorities);
        this.user = user;
    }
}
