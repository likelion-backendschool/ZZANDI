package com.ll.zzandi.dto.board;

import com.ll.zzandi.enumtype.BoardCategory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BoardCreateDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotNull(message = "카테고리 설정은 필수입니다!")
    private String category;

    @Lob
    private String content;
}
