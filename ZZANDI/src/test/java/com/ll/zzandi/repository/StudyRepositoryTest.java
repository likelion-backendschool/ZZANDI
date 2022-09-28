package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.StudyType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudyRepositoryTest {
  @Autowired
  StudyRepository studyRepository;

  @Test
  void 타입에_맞게_스터디목록_출력 () {
    String st = "BOOK";
    String ss = "ALL";
    String kw  = "";

    List<Study> studyList;

    if (st.equals("ALL")) {
      if (ss.equals("ALL")) {
        studyList = studyRepository.findAllByStudyTitleContainsOrUser_userIdContains(kw, kw);
      }
      else {
        studyList = studyRepository.findAllByStudyTitleContainsAndStudyStatusOrUser_userIdContainsAndStudyStatus(kw, StudyStatus.valueOf(ss), kw, StudyStatus.valueOf(ss));
      }
    }
    else {
      if(ss.equals("ALL")) {
        studyList = studyRepository.findAllByUser_userIdContainsAndStudyTypeOrStudyTitleContainsAndStudyType(kw, StudyType.valueOf(st), kw, StudyType.valueOf(st));
      }
      else {
        studyList = studyRepository.findAllByStudyTypeAndStudyStatusAndStudyTitleContainsOrStudyTypeAndStudyStatusAndUser_userIdContains(StudyType.valueOf(st), StudyStatus.valueOf(ss),kw, StudyType.valueOf(st), StudyStatus.valueOf(ss), kw);
      }
    }

    System.out.println(studyList.size());
  }
}
