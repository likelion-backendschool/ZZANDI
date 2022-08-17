package com.ll.zzandi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "STUDY")
@NoArgsConstructor
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDY_ID")
    private Long id;

    @Column(name = "STUDY_TITLE")
    private String studyTitle;

    @Column(name = "BOOK_ID")
    private int bookId;

    @Column(name = "LC_ID")
    private int lcId;

    @Column(name = "STUDY_TYPE")
    private String studyType;

    @Column(name = "STUDY_START")
    private String studyStart;

    @Column(name = "STUDY_END")
    private String studyEnd;

    @Column(name = "STUDY_PEOPLE")
    private int studyPeople;

    @Column(name = "STUDY_TAG")
    private String studyTag;

    @Column(name = "STUDY_RATE")
    private int studyRate;

    @Column(name = "STUDY_STATUS")
    private String studyStatus;

    public Study(Long id, String studyTitle, int bookId, int lcId, String studyType, String studyStart, String studyEnd, int studyPeople, String studyTag, int studyRate, String studyStatus) {
        this.id = id;
        this.studyTitle = studyTitle;
        this.bookId = bookId;
        this.lcId = lcId;
        this.studyType = studyType;
        this.studyStart = studyStart;
        this.studyEnd = studyEnd;
        this.studyPeople = studyPeople;
        this.studyTag = studyTag;
        this.studyRate = studyRate;
        this.studyStatus = studyStatus;
    }
}


