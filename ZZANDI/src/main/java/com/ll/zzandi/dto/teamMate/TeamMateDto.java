package com.ll.zzandi.dto.teamMate;

import com.ll.zzandi.enumtype.TeamMateDelegate;
import com.ll.zzandi.enumtype.TeamMateStatus;
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
public class TeamMateDto {

  private Long id;
  private String userNickname;
  private String userprofileUrl;
  private Integer teamRate;
  private TeamMateStatus teamMateStatus;
  private TeamMateDelegate teamMateDelegate;
}
