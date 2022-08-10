package com.ll.zzandi.controller;

import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.dto.ToDoListDto;
import com.ll.zzandi.service.ToDoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class ToDoListController {
    private final ToDoListService toDoListService;

    @GetMapping()
    public String showToDoMain() {
        return "ToDoList-main";
    }
    @GetMapping("/add")
    public String showToDoForm(ToDoListDto.ToDoListRequest toDoListRequest) {
        return "ToDoList-add";
    }

    @PostMapping("/add")
    @ResponseBody
    public ToDoList addTodo (ToDoListDto.ToDoListRequest toDoListRequest) {
        ToDoList toDoList = toDoListService.save(toDoListRequest);

        return toDoList;
    }
}
