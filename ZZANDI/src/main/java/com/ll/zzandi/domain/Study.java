package com.ll.zzandi.domain;

import static javax.persistence.FetchType.*;

import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.StudyType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "STUDY_RATE", columnDefinition = "integer default 0")
    private int studyRate;

    @Column(name = "VIEWS", columnDefinition = "integer default 0")
    private int views;

    @Column(name = "ACCEPTED_STUDY_MEMBER", columnDefinition = "integer default 1")
    private int acceptedStudyMember;

    @Column(name = "STUDY_COVER_URL")
    private String studyCoverUrl;

    @Column(name = "STUDY_STATUS")
    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_UUID")
    private User user;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMate> teamMateList = new ArrayList<>();

    public Study(User user , String studyTitle, Book book, Lecture lecture, StudyType studyType, String studyStart, String studyEnd, int studyPeople, String studyTag, StudyStatus studyStatus) {
        this.user = user;
        this.studyTitle = studyTitle;
        this.book = book;
        this.lecture = lecture;
        this.studyType = studyType;
        this.studyStart = studyStart;
        this.studyEnd = studyEnd;
        this.studyPeople = studyPeople;
        this.studyTag = studyTag;
        this.studyStatus = studyStatus;
    }
    public void getStudyCoverUrl(String studyCoverUrl) {
        this.studyCoverUrl=studyCoverUrl;
    }

    public Study checkStatus() {
        int startYear = Integer.parseInt(studyStart.substring(0, 4));
        int startMonth = Integer.parseInt(studyStart.substring(5, 7));
        int startDay = Integer.parseInt(studyStart.substring(8, 10));

        int endYear = Integer.parseInt(studyEnd.substring(0, 4));
        int endMonth = Integer.parseInt(studyEnd.substring(5, 7));
        int endDay = Integer.parseInt(studyEnd.substring(8, 10));

        if (LocalDate.now().isEqual(LocalDate.of(startYear, startMonth, startDay))) {
            this.studyStatus = StudyStatus.PROGRESS;
        }

        if (LocalDate.now().isAfter(LocalDate.of(endYear, endMonth, endDay))) {
            this.studyStatus = StudyStatus.COMPLETE;
        }
        return this;
    }

}


