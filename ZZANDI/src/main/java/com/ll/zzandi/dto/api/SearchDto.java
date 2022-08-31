package com.ll.zzandi.dto.api;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDto {
    List<SearchDto.Item> item = new ArrayList<>();
    @Getter
    public static class Item{
        private String title;
        private String cover;
        private String author;
        private String isbn13;
        private String publisher;
    }
}

