package com.ll.zzandi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class BoardWriteDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String content;
}
