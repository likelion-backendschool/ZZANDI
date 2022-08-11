package com.ll.zzandi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "\"Study\"")
@NoArgsConstructor
public class Study extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDY_YYID")
    private Long id;
    @Column(name = "STUDY_TITLE")
    private String studyTitle;
    @Column(name = "STUDY_START")
    private String studyStart;
    @Column(name = "STUDY_END")
    private String studyEnd;

    public String getStudyStart() {
        return studyStart;
    }

    public void setStudyStart(String studyStart) {
        this.studyStart = studyStart;
    }

    public String getStudyEnd() {
        return studyEnd;
    }

    public void setStudyEnd(String studyEnd) {
        this.studyEnd = studyEnd;
    }

    private String studyTag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudyTitle() {
        return studyTitle;
    }

    public void setStudyTitle(String studyTitle) {
        this.studyTitle = studyTitle;
    }

    public String getStudyTag() {
        return studyTag;
    }

    public void setStudyTag(String studyTag) {
        this.studyTag = studyTag;
    }

    public int getStudyPeople() {
        return studyPeople;
    }

    public void setStudyPeople(int studyPeople) {
        this.studyPeople = studyPeople;
    }

    private int studyPeople;
}