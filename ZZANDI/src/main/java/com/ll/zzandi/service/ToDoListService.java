package com.ll.zzandi.service;

import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.dto.ToDoListDto;
import com.ll.zzandi.repository.ToDoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToDoListService {
    private final ToDoListRepository toDoListRepository;
    public ToDoList save(ToDoListDto.ToDoListRequest toDoListRequest) {
        ToDoList toDoList = new ToDoList();

        toDoList.setTitle(toDoListRequest.getTitle());
        toDoList.setContent(toDoListRequest.getContent());
        toDoList.setType(toDoListRequest.getType());
        toDoList.setWriter(toDoListRequest.getWriter());

        return toDoListRepository.save(toDoList);
    }
}
