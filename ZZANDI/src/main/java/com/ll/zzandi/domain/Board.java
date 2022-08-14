package com.ll.zzandi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@Table(name = "QA_BOARD")
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QA_ID")
    private Long id;

    @Column(name = "QA_TITLE")
    private String title;

    @Column(name = "QA_CONTENT")
    private String content;

    @Column(name = "QA_VIEWS")
    private int views;

    @Column(name = "QA_HEART")
    private int heart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_UUID")
    private User user;

    public Board(User user, String title, String content, int views, int heart) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.views = views;
        this.heart = heart;
    }
}