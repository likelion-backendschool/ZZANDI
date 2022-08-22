package com.ll.zzandi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "LECTURE")
@NoArgsConstructor
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LECTURE_ID")
    private Long id;
    @Column(name = "LECTURE_NAME")
    private String lectureName;
    @Column(name = "LECTURE_LECTURER")
    private String lecturer;
    @Column(name = "LECTURE_NUM")
    private Integer lectureNumber;
}
