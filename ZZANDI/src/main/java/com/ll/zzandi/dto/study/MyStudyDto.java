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
public class MyStudyDto {
  private Long studyId;
  private String studyTitle;
  private String studyStart;
  private String studyEnd;
  private String studyTag;
  private String studyCoverUrl;
  private String studyStatus;
//  private Long teamMateId;
}
