package com.ll.zzandi.repository;

import com.ll.zzandi.domain.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoListRepository extends JpaRepository <ToDoList, Long> {

}
