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
    private String userId;
    private String title;
    private String createdDate;
    private String writer;
    private String content;
    private int views;
    private int heart;
    private int count; // 댓글의 개수
    private int pageNum; // 현재 페이지 번호
    private String profile; // 현재 게시글 작성자의 프로필 사진

}