package com.ll.zzandi.dto.study;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyDetailDto {

  private String studyTitle;
  private String leader;
  private int acceptedStudyMember;
  private int studyPeople;
  private String studyStart;
  private String studyEnd;
  private String studyTag;
  private String studyType;
  private int studyRate;
  private int views;
  private String studyCoverUrl;
  private String studyStatus;
  private int studyRecommend;

  private String bookName;
  private Integer bookPage;
  private String bookAuthor;
  private String bookPublisher;
  private String bookIsbn;

  private String lectureName;
  private String lecturer;
  private Integer lectureNumber;

}
