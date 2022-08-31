package com.ll.zzandi.dto.api;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDto {
    List<Item> item = new ArrayList<>();
    @Getter
    public static class Item{
        public String title;
        public String cover;
        public String author;
        public String isbn13;
        public String description;
        public String categoryName;
    }
}

