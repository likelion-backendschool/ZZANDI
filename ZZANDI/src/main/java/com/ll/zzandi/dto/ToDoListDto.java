package com.ll.zzandi.dto;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.enumtype.ToDoType;
import com.ll.zzandi.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class ToDoListDto {
    @Getter
    @Setter
    public static class ToDoListRequest {
        @NotEmpty(message = "목표를 적어주세요!")
        private String content;
        private ToDoType type;

        private LocalDateTime createDate;

        private LocalDateTime updateDate;

        private User user;

        public ToDoListRequest(String content, User user) {
            this.content = content;
            this.type = ToDoType.DOING;
            this.createDate = LocalDateTime.now();
            this.user = user;
        }
    }
    public static class ToDOListResponse {
        private String content;

        private ToDoType type;

        private LocalDateTime createDate;

        private LocalDateTime updateDate;
    }
}
