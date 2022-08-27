package com.ll.zzandi.service;

import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.dto.ToDoListDto;
import com.ll.zzandi.enumtype.ToDoType;
import com.ll.zzandi.repository.ToDoListRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ll.zzandi.enumtype.ToDoType.DOING;
import static com.ll.zzandi.enumtype.ToDoType.DONE;

@Service
@RequiredArgsConstructor
public class ToDoListService {
    private final ToDoListRepository toDoListRepository;
    @Transactional
    public ToDoList save(ToDoListDto.ToDoListRequest toDoListRequest) {
        ToDoList toDoList = new ToDoList();

        toDoList.setContent(toDoListRequest.getContent());
        toDoList.setType(toDoListRequest.getType());

        toDoListRepository.save(toDoList);

        return toDoList;
    }

    public List<ToDoList> findAllByType(ToDoType type) {
        return toDoListRepository.findAllByType(type);
    }

    public List<ToDoList> findAll() {
        return toDoListRepository.findAll();
    }

    public ToDoList changeType(long id) {

        ToDoList toDoList = toDoListRepository.findById(id).get();

        if (toDoList.getType().name().equals("DOING")) {
            toDoList.setType(DONE);
        }
        else toDoList.setType(DOING);

        toDoListRepository.save(toDoList);

        return toDoList;
    }

    public void delete(long id) {
        ToDoList toDoList = toDoListRepository.findById(id).get();

        toDoListRepository.delete(toDoList);
    }

  public ToDoList findById(long id) {
        return toDoListRepository.findById(id).get();
  }

    public ToDoList update(long id, String content) {
        ToDoList toDoList = toDoListRepository.findById(id).get();

        toDoList.setContent(content);
        toDoList.setUpdatedDate(LocalDateTime.now());
        toDoListRepository.save(toDoList);

        return toDoList;
    }
}
