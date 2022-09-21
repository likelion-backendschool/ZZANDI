package com.ll.zzandi.dto.board;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardFileDto {

    private Long fileId;
    private String originName;
    private String ext;
    private String url;
}
