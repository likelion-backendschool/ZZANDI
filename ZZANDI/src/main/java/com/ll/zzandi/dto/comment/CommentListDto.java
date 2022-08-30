package com.ll.zzandi.dto.comment;

import com.ll.zzandi.enumtype.DeleteStatus;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentListDto {

    private Long commentId;
    private Long boardId;
    private Long userUUID;
    private String userId;
    private String profile;
    private String writer;
    private Long parentId;
    private String content;
    private DeleteStatus status;
    private String createdDate;

}
