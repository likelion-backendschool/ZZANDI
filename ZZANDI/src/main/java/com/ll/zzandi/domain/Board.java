package com.ll.zzandi.domain;

import com.ll.zzandi.enumtype.BoardCategory;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    @NotNull
    private String category;

    @Column(name = "TITLE")
    @NotNull
    private String title;

    @Column(name = "CONTENT")
    @Lob
    private String content;

    @Column(name = "VIEWS")
    private int views;

    @Column(name = "HEART")
    private int heart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_UUID")
    private User user;

    @JoinColumn(name = "STUDY_ID")
    @OneToOne(fetch = LAZY)
    private Study study;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    public Board(User user, String category, String title, String content, int views, int heart, Study study) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
        this.views = views;
        this.heart = heart;
        this.study = study;
    }
}