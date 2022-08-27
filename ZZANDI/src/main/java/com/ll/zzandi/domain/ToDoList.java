package com.ll.zzandi.domain;

import static javax.persistence.FetchType.LAZY;

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
public class ToDoList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODO_ID")
    private long id;

    @Column (name = "TODO_CONTENT")
    private String content;

    @Column (name = "TODO_TYPE")
    @Enumerated(EnumType.STRING)
    private ToDoType type;

    @ManyToOne
    @JoinColumn (name = "USER_UUID")
    private User user;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "USER_UUID")
//    private User user;
}
