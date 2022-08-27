package com.ll.zzandi.repository;

import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.enumtype.ToDoType;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ToDoRepositoryTest {

  @Autowired
  ToDoListRepository toDoListRepository;

  @Autowired
  UserRepository userRepository;

  @Test
  void findByTypeTest() {
    List<ToDoList> doingToDoList = toDoListRepository.findAllByType(ToDoType.DOING);

    System.out.println(doingToDoList.size());
  }

  @Test
  void findByUser() {
    User user = userRepository.findByUserId("zzandi").get();

    System.out.println(user.getUserId());

    List<ToDoList> toDoListByUser = toDoListRepository.findAllByUser(user);

    System.out.println(toDoListByUser.size());
    System.out.println(toDoListByUser.get(0));
    System.out.println(toDoListByUser.get(1));
  }

  @Test
  void findByUserAndType() {
    User user = userRepository.findByUserId("zzandi").get();

    List<ToDoList> toDoListByUserAndType = toDoListRepository.findAllByUserAndType(user, ToDoType.DONE);

    System.out.println(toDoListByUserAndType.size());
    System.out.println(toDoListByUserAndType.get(0).getContent());
  }
}
