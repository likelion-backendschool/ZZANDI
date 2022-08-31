package com.ll.zzandi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BookInfoDto {
    List<Item> item = new ArrayList<>();

    public static class Item{
        public String title;
        public String cover;
        public String author;
        public String isbn13;
        public String description;
        public BookInfoDto.subInfo subInfo;
    }
    @Getter
    public static class subInfo{
        @JsonProperty
        String subTitle;
        @JsonProperty
        Integer itemPage;
        @JsonProperty
        String  toc;
    }
}