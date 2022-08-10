package com.ll.zzandi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDto {

    private Long boardId;
    private String title;
    private String writer;
    private String createdDate;
    private int views; // 조회수
    private int recommend; // 추천수

}
