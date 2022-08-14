package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.dto.ToDoListDto;
import com.ll.zzandi.service.ToDoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class ToDoListController {
    private final ToDoListService toDoListService;

    @GetMapping()
    public String showToDoMain(Model model) {
        List<ToDoList> toDoList = toDoListService.findAll();
        model.addAttribute("toDoList", toDoList);
        return "todo/ToDoListMain";
    }

    @PostMapping("/add")
    public String addTodo (ToDoListDto.ToDoListRequest toDoListRequest) {
        ToDoList toDoList = toDoListService.save(toDoListRequest);

        return "redirect:/todo";
    }
}
