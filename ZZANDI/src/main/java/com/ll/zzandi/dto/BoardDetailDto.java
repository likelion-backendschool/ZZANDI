package com.ll.zzandi.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private int page; // 현재 페이지 번호
    private Long prev; // 이전 게시물 번호
    private Long next; // 다음 게시물 번호
    private String profile; // 현재 게시글 작성자의 프로필 사진

}