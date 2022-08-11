package com.ll.zzandi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyRequestDto {
    private String studyId;
    private String studyTitle;
    private Date studyStart;
    private Date studyEnd;
    private int studyPeople;
    private String studyTag;

}