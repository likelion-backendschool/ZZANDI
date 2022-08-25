package com.ll.zzandi.domain;

import com.ll.zzandi.enumtype.ToDoType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "TO_DO_LIST")
@NoArgsConstructor
@ToString
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODO_ID")
    private long id;

    @Column (name = "TODO_CONTENT")
    private String content;

    @Column (name = "TODO_TYPE")
    @Enumerated(EnumType.STRING)
    private ToDoType type;
}
