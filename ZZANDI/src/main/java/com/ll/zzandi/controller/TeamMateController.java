package com.ll.zzandi.controller;

import com.ll.zzandi.domain.User;
import com.ll.zzandi.service.TeamMateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("{studyId}/teamMate")
public class TeamMateController {

  private final TeamMateService teamMateService;

  /*
  스터디 참가 신청
   */
  @PostMapping("/create")
  public String createTeamMate(@AuthenticationPrincipal User user, @PathVariable Long studyId, Model model) {
    teamMateService.createTeamMate(user, studyId);
    return "redirect:/study/detail/%d".formatted(studyId);
  }

  /*
  스터디 참가 수락
    */
  @PostMapping("/update/{teamMateId}")
  public String updateTeamMate(@AuthenticationPrincipal User user, @PathVariable Long studyId, @PathVariable Long teamMateId, Model model) {
    teamMateService.updateTeamMate(user, studyId, teamMateId);
    return "redirect:/study/detail/%d".formatted(studyId);
  }

  /*
  스터디 참가 거절 및 취소
   */
  @PostMapping("/delete/{teamMateId}")
  public String deleteTeamMate(@AuthenticationPrincipal User user, @PathVariable Long studyId, @PathVariable Long teamMateId) {
    boolean isLeader = teamMateService.deleteTeamMate(user, studyId, teamMateId);
    if (isLeader) {
      return "redirect:/study/detail/%d".formatted(studyId);
    }
    return "redirect:/user/mypage?userNickname=%s".formatted(user.getUserNickname());
  }

  /*
스터디 탈퇴
 */
  @PostMapping("/quit")
  public String quitTeamMate(@AuthenticationPrincipal User user, @PathVariable Long studyId) {
    teamMateService.quitTeamMate(user, studyId);
    return "redirect:/study/detail/%d".formatted(studyId);
  }

  /*
  팀원에게 위임 신청
   */
  @PostMapping("/delegate/{teamMateId}")
  public String delegateTeamMate(@AuthenticationPrincipal User user, @PathVariable Long studyId, @PathVariable Long teamMateId) {
    teamMateService.delegateTeamMate(user, studyId, teamMateId);
    return "redirect:/study/detail/%d".formatted(studyId);
  }

  /*
  팀장 위임 수락
   */
  @PostMapping("/delegate")
  public String delegateTeamMateAccept(@AuthenticationPrincipal User user, @PathVariable Long studyId) {
    teamMateService.delegateTeamMateAccept(user, studyId);
    return "redirect:/study/detail/%d".formatted(studyId);
  }

  /*
  팀장 위임 거절
   */
  @PostMapping("/delegate/refuse")
  public String delegateRefuse(@AuthenticationPrincipal User user, @PathVariable Long studyId) {
    teamMateService.delegateRefuse(user, studyId);
    return "redirect:/study/detail/%d".formatted(studyId);
  }
}
