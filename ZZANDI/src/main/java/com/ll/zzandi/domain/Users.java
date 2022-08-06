package com.ll.zzandi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Users extends BaseEntity{
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
    private Role userRole;

    public Users(String userId, String userPassword, String userEmail, String userNickname) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.userRole = Role.USER;
    }

    public enum Role{
        USER, ADMIN
    }
}
