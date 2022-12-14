package com.ll.zzandi.domain;

import com.ll.zzandi.dto.UserDto;
import com.ll.zzandi.enumtype.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "\"User\"")
@NoArgsConstructor
@ToString
@SQLDelete(sql="UPDATE user SET DELETE_DATE = NOW() where USER_UUID=?")
@Where(clause="DELETE_DATE is NULL")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_UUID")
    private Long id;

    @Column(name = "USER_ID")
    @NotNull
    private String userId;

    @Column(name = "USER_PWD")
    @NotNull
    private String userPassword;

    @Column(name = "USER_EMAIL")
    @NotNull
    private String userEmail;

    @Column(name = "USER_NICKNAME")
    @NotNull
    private String userNickname;

    @Column(name = "USER_ROLE")
    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole userRole;

    @Column(name="USER_EMAILVERIFIED")
    @NotNull
    private boolean userEmailVerified;

    @Column(name="USER_EMAILCHECKTOKEN")
    private String emailCheckToken;

    @Column(name="USER_EMAILCHECKGENERATEDATE")
    private LocalDateTime emailCheckTokenGeneratedAt;

    @Column(name="USER_PROFILEURL")
    private String userprofileUrl;

    @Column(name="USER_ZZANDI")
    private Integer userZzandi;

    @Column(name="DELETE_DATE")
    private LocalDateTime deletedDate;


    public User(String userId, String userPassword, String userEmail, String userNickname) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.userRole = UserRole.ROLE_USER;
        this.userEmailVerified = false;
    }

    public static User of(UserDto.RegisterRequest userDto){
        return new User(userDto.getUserId(), userDto.getUserPassword(), userDto.getUserEmail(), userDto.getUserNickname());
    }
    public UserDto.RegisterResponse toResponse(){
        return new UserDto.RegisterResponse(this);
    }

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }
    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }
    public void completeSignUp() {
        this.userEmailVerified = true;
    }

    public void updateImageUrl(String image_url) {
        this.userprofileUrl=image_url;
    }

    public void updateUserZzandi(Integer userZzandi){
        this.userZzandi = userZzandi;
    }
}
