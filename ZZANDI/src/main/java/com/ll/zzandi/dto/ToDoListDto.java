package com.ll.zzandi.dto;

import com.ll.zzandi.enumtype.Type;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class ToDoListDto {
    @Getter
    @Setter
    public static class ToDoListRequest {
        @NotEmpty(message = "목표를 적어주세요!")
        private String content;
        private Type type;

        public ToDoListRequest(String content) {
            this.content = content;
            this.type = Type.DOING;
        }
    }
    public static class ToDOListResponse {

    }
}
