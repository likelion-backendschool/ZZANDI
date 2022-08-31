package com.ll.zzandi.dto.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDto {
    public Integer totalResults;

    List<Item> item = new ArrayList<>();

    static class Item{
        public String title;
        public String cover;
        public String author;
        public String isbn13;
        public String description;
        public String categoryName;
    }
}

