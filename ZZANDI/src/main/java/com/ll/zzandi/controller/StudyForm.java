package com.ll.zzandi.controller;

import java.util.Date;

public class StudyForm {
    private String startDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    private String endDate;
    private int people;
    private String studyTag;
    private String title;


    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getStudyTag() {
        return studyTag;
    }

    public void setStudyTag(String studyTag) {
        this.studyTag = studyTag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
