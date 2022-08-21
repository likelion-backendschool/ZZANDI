package com.ll.zzandi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class CommentDto {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class commentListDto {
        private Long commentId;
        private Long boardId;
        private Long userUuid;

        @NotEmpty(message = "댓글 내용을 입력해주세요.")
        private String content;

        private String status;
    }
}
