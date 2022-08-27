package com.ll.zzandi.domain;

import static javax.persistence.FetchType.LAZY;

import com.ll.zzandi.enumtype.TeamMateStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TEAMMATE")
@NoArgsConstructor
public class TeamMate extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "TEAMMATE_ID")
  private Long id;

  @ManyToOne(targetEntity = User.class, fetch = LAZY)
  @JoinColumn(name = "USER_UUID")
  private User user;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "STUDY_ID")
  private Study study;

  @Column(name = "TEAMMAATE_RATE")
  private Integer teamRate;

  @Enumerated(EnumType.STRING)
  @Column(name = "TEAMMATE_STATUS")
  private TeamMateStatus teamMateStatus;

  public TeamMate(User user, Study study, Integer teamRate,
      TeamMateStatus teamMateStatus) {
    this.user = user;
    this.study = study;
    this.teamRate = teamRate;
    this.teamMateStatus = teamMateStatus;
  }
}
