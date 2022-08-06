package com.ll.zzandi.domain;

import com.ll.zzandi.dto.UserRequestDto;
import com.ll.zzandi.enumtype.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "\"User\"")
@NoArgsConstructor
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_UUID")
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_PWD")
    private String userPassword;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "USER_NICKNAME")
    private String userNickname;

    @Column(name = "USER_ROLE")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String userId, String userPassword, String userEmail, String userNickname) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.userRole = UserRole.USER;
    }

    public static User of(UserRequestDto userRequestDto){
        return new User(userRequestDto.getUserId(),userRequestDto.getUserPassword(),userRequestDto.getUserEmail(), userRequestDto.getUserNickname());
    }
}
