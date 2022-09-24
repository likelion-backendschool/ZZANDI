package com.ll.zzandi.service;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.TeamMate;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.teamMate.TeamMateDto;
import com.ll.zzandi.enumtype.StudyStatus;
import com.ll.zzandi.enumtype.TeamMateDelegate;
import com.ll.zzandi.enumtype.TeamMateStatus;
import com.ll.zzandi.exception.ErrorType;
import com.ll.zzandi.exception.StudyException;
import com.ll.zzandi.exception.TeamMateException;
import com.ll.zzandi.exception.UserApplicationException;
import com.ll.zzandi.repository.StudyRepository;
import com.ll.zzandi.repository.TeamMateRepository;
import com.ll.zzandi.repository.UserRepository;
import com.ll.zzandi.util.mail.EmailMessage;
import com.ll.zzandi.util.mail.EmailService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamMateService {

  private final TeamMateRepository teamMateRepository;
  private final StudyRepository studyRepository;
  private final EmailService emailService;
  private final StudyService studyService;
  private final UserService userService;
  private final UserRepository userRepository;

  public void createTeamMate(User user, Long studyId) {
    Study study = studyRepository.findById(studyId).orElseThrow(()-> new StudyException(ErrorType.NOT_FOUND));
    TeamMate teamMate = teamMateRepository.findByUserAndAndStudy(user, study).orElse(null);

    // 팀장과 일치하는 경우, Accepted인 상태로 팀원에 팀장 추가
    if (study.getUser().getId().equals(user.getId()) && teamMate == null) {
      teamMateRepository.save(new TeamMate(user, study, TeamMateStatus.ACCEPTED));
    } else if (teamMate == null) {
      teamMateRepository.save(new TeamMate(user, study, TeamMateStatus.WAITING));
      sendWaitingEmail(user, study);
    } else {
      throw new TeamMateException(ErrorType.DUPLICATED_TEAMMATE);
    }

    if (study.getStudyStatus().equals(StudyStatus.RECRUIT_COMPLETE)) {
      throw new TeamMateException(ErrorType.FULL_STUDY);
    }
  }

  public void updateTeamMate(User user, Long studyId, Long teamMateId) {
    Study study = studyRepository.findById(studyId).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));
    TeamMate teamMate = teamMateRepository.findById(teamMateId).orElseThrow(()-> new TeamMateException(ErrorType.NOT_FOUND));

    // 팀장만 수락이 가능
    if(study.getUser().getId().equals(user.getId())) {
      teamMate.setTeamMateStatus(TeamMateStatus.ACCEPTED);
      teamMateRepository.save(teamMate);
      sendAcceptedEmail(study, teamMate);
      study.setAcceptedStudyMember(study.getAcceptedStudyMember()+1);
    }

    studyService.updateRecruitStudyStatus(study);
  }

  public void updateTeamMateRate(User user, Long studyId, int rateInput) {
    Study study = studyRepository.findById(studyId).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));
    if (study.getStudyStatus() != StudyStatus.PROGRESS) {
      throw new TeamMateException(ErrorType.NOT_LEADER);
    }
    TeamMate teamMate = teamMateRepository.findByUserAndAndStudy(user, study).orElseThrow(()-> new TeamMateException(ErrorType.NOT_FOUND));

    teamMate.setTeamRate(rateInput);
    teamMate.setTeamMateDailyCheck("O");
    teamMateRepository.save(teamMate);
  }

  public boolean deleteTeamMate(User user, Long studyId, Long teamMateId) {
    Study study = studyRepository.findById(studyId).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));
    TeamMate teamMate = teamMateRepository.findById(teamMateId).orElseThrow(()-> new TeamMateException(ErrorType.NOT_FOUND));

    boolean isLeader = false;

    if(study.getUser().getId().equals(user.getId())) {
      teamMateRepository.delete(teamMate);
      isLeader = true;
    } else if (user.getId().equals(teamMate.getUser().getId())) {
      teamMateRepository.delete(teamMate);
    }
    return isLeader;
  }

  public void quitTeamMate(User user, Long studyId) {
    Study study = studyRepository.findById(studyId).orElseThrow(()-> new StudyException(ErrorType.NOT_FOUND));
    TeamMate teamMate = teamMateRepository.findByUserAndAndStudy(user, study).orElseThrow(()-> new TeamMateException(ErrorType.NOT_FOUND));

    if((study.getStudyStatus() == StudyStatus.RECRUIT
        || study.getStudyStatus() == StudyStatus.RECRUIT_COMPLETE) && !study.getUser().getId().equals(user.getId())) {
      teamMateRepository.delete(teamMate);
      study.setAcceptedStudyMember(study.getAcceptedStudyMember()-1);
      studyService.updateRecruitStudyStatus(study);
    }

    // 팀장의 경우 추가 예정
    // PROGRESS의 경우, 달성률 관련 개발 진행 후 추가 예정
  }

  public void delegateTeamMate(User user, Long studyId, Long teamMateId) {
    Study study = studyRepository.findById(studyId).orElseThrow(()-> new StudyException(ErrorType.NOT_FOUND));
    TeamMate teamMate = teamMateRepository.findByUserAndAndStudy(user, study).orElseThrow(()-> new TeamMateException(ErrorType.NOT_FOUND));
    TeamMate delegateTeamMate = teamMateRepository.findById(teamMateId).orElseThrow(()-> new TeamMateException(ErrorType.NOT_FOUND));
    User delegateUser = delegateTeamMate.getUser();

    if (study.getUser().getId().equals(user.getId()) && !study.getUser().getId().equals(delegateUser.getId())) {
      teamMate.setTeamMateDelegate(TeamMateDelegate.DELEGATE);
      delegateTeamMate.setTeamMateDelegate(TeamMateDelegate.WAITING);
      sendDelegateEmail(study, user, delegateUser);
      teamMateRepository.saveAll(Arrays.asList(teamMate, delegateTeamMate));
    }
  }

  public void delegateTeamMateAccept(User user, Long studyId) {
    Study study = studyRepository.findById(studyId).orElseThrow(()-> new StudyException(ErrorType.NOT_FOUND));
    User prev = study.getUser();
    TeamMate teamMate = teamMateRepository.findByUserAndAndStudy(prev, study).orElseThrow(()-> new TeamMateException(ErrorType.NOT_FOUND));

    teamMateRepository.delete(teamMate);
    study.setAcceptedStudyMember(study.getAcceptedStudyMember()-1);
    study.setUser(user);
    List<TeamMate> teamMateList = teamMateRepository.findByStudy(study);
    for (TeamMate teamMate1 : teamMateList) {
      teamMate1.setTeamMateDelegate(TeamMateDelegate.NONE);
      teamMateRepository.save(teamMate1);
    }
    studyService.updateRecruitStudyStatus(study);
    sendDelegateAcceptEmail(prev, study, user);
  }

  public void delegateRefuse(User user, Long studyId) {
    Study study = studyRepository.findById(studyId).orElseThrow(()-> new StudyException(ErrorType.NOT_FOUND));
    TeamMate teamMate = teamMateRepository.findByUserAndAndStudy(user, study).orElseThrow(()-> new TeamMateException(ErrorType.NOT_FOUND));
    teamMate.setTeamMateDelegate(TeamMateDelegate.NONE);
    teamMateRepository.save(teamMate);
  }

  private void sendDelegateAcceptEmail(User user, Study study, User delegateUser) {
    String message = "안녕하세요. %s님, <br/>".formatted(user.getUserNickname())
        + "%s님이 [%s] 스터디 팀장 권한 위임을 수락하셨습니다.<br/>".formatted(delegateUser.getUserNickname(), study.getStudyTitle())
        + "따라서, 해당 스터디에서의 탈퇴가 정상적으로 처리되었습니다.";

    EmailMessage emailMessage = EmailMessage.builder()
        .to(user.getUserEmail())
        .subject("ZZANDI, 스터디 팀장 권한 위임 수락 알림")
        .message(message)
        .build();
    emailService.sendEmail(emailMessage);
  }

  private void sendDelegateEmail(Study study, User user, User delegateUser) {
    String message = "안녕하세요. %s님, <br/>".formatted(delegateUser.getUserNickname())
        + "%s님이 [%s] 스터디 팀장 권한 위임을 신청하셨습니다.<br/>".formatted(user.getUserNickname(), study.getStudyTitle())
        + "ZZANDI로 접속하여 %s님 신청을 수락 및 거절해주길 바랍니다.<br/>".formatted(user.getUserNickname())
        + "http://localhost:8080/study/detail/%d".formatted(study.getId());

    EmailMessage emailMessage = EmailMessage.builder()
        .to(delegateUser.getUserEmail())
        .subject("ZZANDI, 스터디 팀장 권한 위임 알림")
        .message(message)
        .build();
    emailService.sendEmail(emailMessage);
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

  public List<TeamMate> findAllByUser(User user) {
    return teamMateRepository.findAllByUser(user);
  }

  public List<TeamMateDto> findByStudy(Long studyId) {
    Study study = studyRepository.findById(studyId)
        .orElseThrow(() -> new StudyException(ErrorType.NOT_FOUND));

    return teamMateRepository.findByStudy(study).stream().map(
            teamMate -> new TeamMateDto(teamMate.getId(), teamMate.getUser().getUserNickname(),
                teamMate.getUser().getUserprofileUrl(), teamMate.getTeamRate(),
                teamMate.getTeamMateStatus(), teamMate.getTeamMateDelegate()))
        .collect(Collectors.toList());
  }

  public List<Boolean> checkTeamMate(List<TeamMate> teamMateList, User user) {

    boolean isParticipation =false;
    boolean isTeamMate =false;
    boolean isDelete = false;
    int cnt = 0;

    for(TeamMate teamMate : teamMateList) {
      if(teamMate.getUser().getId().equals(user.getId())) {
        isParticipation =true;
        if(teamMate.getTeamMateStatus().equals(TeamMateStatus.ACCEPTED)) {
          isTeamMate =true;
        }
      }
      if (teamMate.getTeamMateStatus().equals(TeamMateStatus.ACCEPTED)) {
        cnt += 1;
      }
    }

    if (cnt == 1) {
      isDelete = true;
    }
    return Arrays.asList(isParticipation, isTeamMate, isDelete);
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void updateDailyCheck() {
    List<TeamMate> teamMates = teamMateRepository.findAll();
    for (TeamMate teamMate : teamMates) {
      if(teamMate.getTeamMateDailyCheck().equals("X")){
        teamMate.getUser().setUserZzandi( teamMate.getUser().getUserZzandi()-1);
      } else {
        teamMate.getUser().setUserZzandi( teamMate.getUser().getUserZzandi()+1);
      }
      teamMate.setTeamMateDailyCheck("X");
      teamMateRepository.save(teamMate);
    }
  }

  public int findRateByUserId(User user, Long studyId) {
    Study study = studyRepository.findById(studyId).orElseThrow(()->new StudyException(ErrorType.NOT_FOUND));

    if (study.getStudyStatus() != StudyStatus.PROGRESS) {
      throw new TeamMateException(ErrorType.NOT_LEADER);
    }

    TeamMate teamMate = teamMateRepository.findByUserAndAndStudy(user, study).orElseThrow(()-> new TeamMateException(ErrorType.NOT_FOUND));

    return teamMate.getTeamRate();
  }
}
