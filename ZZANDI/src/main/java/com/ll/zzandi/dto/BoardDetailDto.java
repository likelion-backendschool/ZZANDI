package com.ll.zzandi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailDto {

    private Long boardId;
    private Long userUUID;
    private String title;
    private String createdDate;
    private String writer;
    private String content;
    private int views;
    private int heart;
    private int comments; // 댓글 수
    private int pageNum; // 현재 페이지 번호

}