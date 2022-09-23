package com.ll.zzandi.dto.board;

import com.ll.zzandi.enumtype.BoardCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardUpdateFormDto {

    private Long id;
    private String category;
    private String title;
    private String writer;
    private String content;
    private int pageNum;

    private LocalDateTime updatedDate;

}
