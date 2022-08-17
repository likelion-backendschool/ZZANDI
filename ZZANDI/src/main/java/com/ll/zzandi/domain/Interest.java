package com.ll.zzandi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_uuid")
    private User user;

    @Column(name = "IT_INTEREST")
    private String interest;
}
