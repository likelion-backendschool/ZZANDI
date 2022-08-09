package com.ll.zzandi.domain;

import com.ll.zzandi.enumtype.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "TO_DO_LIST")
@NoArgsConstructor
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODO_ID")
    private long id;

    @Column (name = "TODO_TITLE")
    private String title;

    @Column (name = "TODO_CONTENT")
    private String content;

    @Column (name = "TODO_WRITER")
    private String writer;

    @Column (name = "TODO_TYPE")
    @Enumerated(EnumType.STRING)
    private Type type;
}
