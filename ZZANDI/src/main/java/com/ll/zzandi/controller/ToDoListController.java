package com.ll.zzandi.controller;

import com.ll.zzandi.dto.ToDoListDto;
import com.ll.zzandi.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/todo")
public class ToDoListController {

    @GetMapping("/add")
    public String showToDoForm(ToDoListDto.ToDoListRequest toDoListRequest) {
        return "ToDoList";
    }
}
