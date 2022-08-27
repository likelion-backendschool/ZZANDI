package com.ll.zzandi.service;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.TeamMate;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.TeamMateStatus;
import com.ll.zzandi.exception.TeamMateException;
import com.ll.zzandi.repository.StudyRepository;
import com.ll.zzandi.repository.TeamMateRepository;
import com.ll.zzandi.repository.UserRepository;
import com.ll.zzandi.util.mail.EmailMessage;
import com.ll.zzandi.util.mail.EmailService;
import jdk.jshell.spi.ExecutionControl.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamMateService {

  private final TeamMateRepository teamMateRepository;
  private final UserRepository userRepository;
  private final StudyRepository studyRepository;
  private final EmailService emailService;
  private final StudyService studyService;

  public void createTeamMate(User user, Long studyId) {
    User currentUser = userRepository.findByUserId(user.getUserId()).orElseThrow(RuntimeException::new);
    Study study = studyRepository.findById(studyId).orElseThrow(RuntimeException::new);
    TeamMate teamMate = teamMateRepository.findByUserAndAndStudy(currentUser, study).orElse(null);

    // 팀장과 일치하는 경우, Accepted인 상태로 팀원에 팀장 추가
    if (study.getUser().getId().equals(currentUser.getId()) && teamMate == null) {
      teamMateRepository.save(new TeamMate(user, study, 0, TeamMateStatus.ACCEPTED));
    } else if (teamMate == null) {
      teamMateRepository.save(new TeamMate(user, study, 0, TeamMateStatus.WAITING));
      sendWaitingEmail(currentUser, study);
    } else {
      throw new TeamMateException("이미 신청한 스터디입니다.");
    }

    if (study.getStudyStatus().equals(StudyStatus.RECRUIT_COMPLETE)) {
      throw new TeamMateException("팀원이 모두 모집되어 신청할 수 없습니다.");
    }
  }

  public void updateTeamMate(User user, Long studyId, Long teamMateId) {
    User currentUser = userRepository.findByUserId(user.getUserId()).orElseThrow(RuntimeException::new);
    Study study = studyRepository.findById(studyId).orElseThrow(RuntimeException::new);
    TeamMate teamMate = teamMateRepository.findById(teamMateId).orElseThrow(RuntimeException::new);

    // 팀장만 수락이 가능
    if(study.getUser() == currentUser) {
      teamMate.setTeamMateStatus(TeamMateStatus.ACCEPTED);
      teamMateRepository.save(teamMate);
      sendAcceptedEmail(study, teamMate);
    }

    Integer teamMateCount = teamMateRepository.countByStudyAndTeamMateStatus(study, TeamMateStatus.ACCEPTED);
    if (teamMateCount == study.getStudyPeople()) {
      studyService.updateStudyStatusRecruitComplete(study);
    }
  }

  private void sendAcceptedEmail(Study study, TeamMate teamMate) {
    String message = "안녕하세요. %s님, <br/>".formatted(teamMate.getUser().getUserNickname())
        + "%s님이 [%s] 스터디 참가 신청을 수락했습니다.<br/>".formatted(study.getUser().getUserNickname(), study.getStudyTitle())
        + "팀원이 되신 것을 축하드리며, 스터디 시작일은 %s입니다.<br/>".formatted(study.getStudyStart())
        + "자세한 스터디 세부 정보는 아래의 링크로 접속하셔서 확인하시길 바랍니다. <br/>"
        + "http://localhost:8080/study/detail/%d".formatted(study.getId());

    EmailMessage emailMessage = EmailMessage.builder()
        .to(teamMate.getUser().getUserEmail())
        .subject("ZZANDI, 스터디 신청 수락 알림")
        .message(message)
        .build();
    emailService.sendEmail(emailMessage);
  }

  private void sendWaitingEmail(User user, Study study) {

    String message = "안녕하세요. %s님, <br/>".formatted(study.getUser().getUserNickname())
        + "%s님이 [%s] 스터디에 참가 신청했습니다.<br/>".formatted(user.getUserNickname(), study.getStudyTitle())
        + "%s님을 수락하시려면, ZZANDI로 접속하여 수락해주세요.<br/>".formatted(user.getUserNickname())
        + "http://localhost:8080/study/detail/%d".formatted(study.getId());

    EmailMessage emailMessage = EmailMessage.builder()
        .to(study.getUser().getUserEmail())
        .subject("ZZANDI, 스터디 신청 알림")
        .message(message)
        .build();
    emailService.sendEmail(emailMessage);
  }
}