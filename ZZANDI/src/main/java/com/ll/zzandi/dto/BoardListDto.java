package com.ll.zzandi.dto;

import com.ll.zzandi.enumtype.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDto {

    private Long boardId;
    private BoardCategory category;
    private String title;
    private String writer;
    private String createdDate;
    private int views; // 조회수
    private int heart; // 좋아요 수
    private int pageNum; // 현재 페이지 번호
    private int count; // 댓글의 개수

}
