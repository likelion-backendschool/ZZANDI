package com.ll.zzandi.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class File {
    @Id
    @GeneratedValue
    private Long id;
}
