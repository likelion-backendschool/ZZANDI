package com.ll.zzandi.domain;

import com.ll.zzandi.enumtype.BoardCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@Table(name = "BOARD")
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    @NotNull
    private BoardCategory category;

    @Column(name = "TITLE")
    @NotNull
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "VIEWS")
    private int views;

    @Column(name = "HEART")
    private int heart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_UUID")
    private User user;

    public Board(User user, BoardCategory category, String title, String content, int views, int heart) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.views = views;
        this.heart = heart;
    }
}