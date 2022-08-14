package com.ll.zzandi.service;

import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.dto.ToDoListDto;
import com.ll.zzandi.repository.ToDoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoListService {
    private final ToDoListRepository toDoListRepository;
    @Transactional
    public ToDoList save(ToDoListDto.ToDoListRequest toDoListRequest) {
        ToDoList toDoList = new ToDoList();

        toDoList.setContent(toDoListRequest.getContent());
        toDoList.setType(toDoListRequest.getType());

        return toDoListRepository.save(toDoList);
    }

    public List<ToDoList> findAll() {
        return toDoListRepository.findAll();
    }
}
