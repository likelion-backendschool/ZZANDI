package com.ll.zzandi.dto;

import com.ll.zzandi.enumtype.Type;
import lombok.Getter;
import lombok.Setter;

public class ToDoListDto {
    // title , content, writer, type
    @Getter
    @Setter
    public static class ToDoListRequest {
        private String content;
        private Type type;


        public ToDoListRequest(String title, String content, String writer) {
            this.content = content;
            this.type = Type.DOING;
        }
    }
    public static class ToDOListResponse {

    }
}
