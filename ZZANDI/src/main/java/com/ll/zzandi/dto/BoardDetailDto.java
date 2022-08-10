package com.ll.zzandi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailDto {

    private Long id;
    private String title;
    private String createdDate;
    private String writer;
    private String content;
    private int views;
    private int recommend;
    private int comments; // 댓글 수

}
