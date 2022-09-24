package com.ll.zzandi.controller;

import com.ll.zzandi.domain.TeamMate;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.teamMate.TeamMateDto;
import com.ll.zzandi.service.TeamMateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
  개인 진도율 수정
  */
  @GetMapping("/update/{rateInput}")
  @ResponseBody
  public void updateTeamMateRate(@AuthenticationPrincipal User user, @PathVariable Long studyId, @PathVariable int rateInput, Model model) {
    teamMateService.updateTeamMateRate(user, studyId, rateInput);
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

  /*
  팀원 목록
   */
  @GetMapping("/data")
  @ResponseBody
  public List<TeamMateDto> findTeamMateList(@PathVariable Long studyId) {
    return teamMateService.findByStudy(studyId);
  }

  @GetMapping("/rate-data")
  @ResponseBody
  public int findRateData(@AuthenticationPrincipal User user, @PathVariable Long studyId) {
    return teamMateService.findRateByUserId(user, studyId);
  }
}
