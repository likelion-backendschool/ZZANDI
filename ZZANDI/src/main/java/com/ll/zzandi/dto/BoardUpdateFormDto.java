package com.ll.zzandi.dto;

import com.ll.zzandi.enumtype.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateFormDto {

    private Long id;
    private BoardCategory category;
    private String title;
    private String writer;
    private String content;

    private LocalDateTime updatedDate;

}
