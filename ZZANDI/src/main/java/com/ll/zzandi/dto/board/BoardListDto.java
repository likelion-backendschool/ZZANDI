package com.ll.zzandi.dto.board;

import com.ll.zzandi.domain.File;
import com.ll.zzandi.enumtype.BoardCategory;
import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListDto {

    private Long boardId;
    private String userId;
    private String category;
    private String title;
    private String writer;
    private String createdDate;
    private int views;
    private int heart;
    private int pageNum;
    private int count;
    private String profile;
    private List<File> files;
    private int existCount;
}
