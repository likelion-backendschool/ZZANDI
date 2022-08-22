package com.ll.zzandi.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "BOOK")
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    private Long id;
    @Column(name = "BOOK_NAME")
    private String bookName;
    @Column(name = "BOOK_PAGE")
    private Integer bookPage;
    @Column(name = "BOOK_AUTHOR")
    private String bookAuthor;
    @Column(name = "BOOK_PUBLISHER")
    private String bookPublisher;
    @Column(name = "BOOK_URL")
    private String bookUrl;
}
