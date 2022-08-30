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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_uuid")
    private User user;

    @Column(name = "CM_PARENT_ID")
    private Long parentId; // 부모 댓글 id

    @Column(name = "CM_CONTENT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "CM_STATUS")
    private DeleteStatus deleteStatus; // 댓글 삭제 여부

    @LastModifiedDate
    @Column(name = "DELETED_DATE")
    private LocalDateTime deletedDate; // 삭제일

    public void changeComment(Comment comment) {
        this.content = comment.getContent();
    }

    public Comment(Board board, User user, Long parentId, String content, DeleteStatus deleteStatus) {
        this.board = board;
        this.user = user;
        this.parentId = parentId;
        this.content = content;
        this.deleteStatus = deleteStatus;
    }
}
