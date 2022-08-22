package com.ll.zzandi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String bookName;
    private Integer bookPage;
    private String bookAuthor;
    private String bookPublisher;
    private String bookUrl;
}


