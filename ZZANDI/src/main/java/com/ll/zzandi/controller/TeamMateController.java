package com.ll.zzandi.controller;

import com.ll.zzandi.domain.TeamMate;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.service.TeamMateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("{studyId}/teamMate")
public class TeamMateController {

  private final TeamMateService teamMateService;

  /*
  스터디 참가 신청
   */
  @GetMapping("/create")
  public String createTeamMate(@AuthenticationPrincipal User user, @PathVariable Long studyId, Model model) {
    teamMateService.createTeamMate(user, studyId);
    return "redirect:/study/detail/%d".formatted(studyId);
  }

  /*
  스터디 참가 수락
    */
  @GetMapping("/update/{teamMateId}")
  public String updateTeamMate(@AuthenticationPrincipal User user, @PathVariable Long studyId, @PathVariable Long teamMateId, Model model) {
    teamMateService.updateTeamMate(user, studyId, teamMateId);
    return"redirect:/study/detail/%d".formatted(studyId);
  }
}