package com.ll.zzandi.domain;

import com.ll.zzandi.enumtype.DeleteStatus;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@Table(name = "COMMENT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CM_ID")
    private Long id; // 댓글 id

    @Lob
    @Column(name = "CM_CONTENT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "CM_STATUS")
    private DeleteStatus deleteStatus; // 댓글 삭제 여부

    @Column(name = "DELETED_DATE")
    private LocalDateTime deletedDate; // 삭제일

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_UUID")
    private User user;

    @Column(name = "CM_REF")
    private Long ref; // 댓글 그룹 번호 (부모 댓글의 ID가 아님! 1번부터 1씩 증가!)

    @Column(name = "CM_STEP")
    private Long step; // 댓글의 계층 (대댓글, 대대댓글...)

    @Column(name = "CM_REF_ORDER")
    private Long refOrder; // 그룹별 댓글의 순서

    @Column(name = "CM_COUNT")
    private Long count; // 자식 댓글의 개수

    @Column(name = "CM_PARENT_ID")
    private Long parentId; // 부모 댓글의 PK

    public void changeComment(Comment comment) {
        this.content = comment.getContent();
    }
    public void reduceCount() {
        --count;
    }
}
