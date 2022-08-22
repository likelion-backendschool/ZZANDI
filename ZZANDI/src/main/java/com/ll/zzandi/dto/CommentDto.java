package com.ll.zzandi.dto;

import com.ll.zzandi.enumtype.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class CommentDto {

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String content;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long commentId;
        private Long boardId;
        private Long userUuid;
        private String writer;

        private Long parentId; // 부모 댓글의 아이디

        @NotEmpty(message = "댓글 내용을 입력해주세요.")
        private String content;
        private CommentStatus status;
        private LocalDateTime createdDate;
    }
}
