package com.ll.zzandi.repository;

import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.enumtype.ToDoType;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoListRepository extends JpaRepository <ToDoList, Long> {

    @Query("select t from ToDoList t where t.type = :type")
    List<ToDoList> findAllByType(@Param("type") ToDoType type);


    List<ToDoList> findAllByUser(User user);

    List<ToDoList> findAllByUserAndType(User user, ToDoType type);
}