package com.ll.zzandi.domain;

import static javax.persistence.FetchType.*;

import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.StudyType;
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

    @JoinColumn(name = "BOOK_ID")
    @OneToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    private Book book;

    @JoinColumn(name = "LC_ID")
    @OneToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    private Lecture lecture;

    @Column(name = "STUDY_TYPE")
    @Enumerated(EnumType.STRING)
    private StudyType studyType;

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
    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    public Study(String studyTitle, Book book, Lecture lecture, StudyType studyType, String studyStart, String studyEnd, int studyPeople, String studyTag, int studyRate, StudyStatus studyStatus) {
        this.studyTitle = studyTitle;
        this.book = book;
        this.lecture = lecture;
        this.studyType = studyType;
        this.studyStart = studyStart;
        this.studyEnd = studyEnd;
        this.studyPeople = studyPeople;
        this.studyTag = studyTag;
        this.studyRate = studyRate;
        this.studyStatus = studyStatus;
    }
}


