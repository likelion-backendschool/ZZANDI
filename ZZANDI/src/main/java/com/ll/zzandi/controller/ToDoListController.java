package com.ll.zzandi.controller;

import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.dto.ToDoListDto;
import com.ll.zzandi.enumtype.ToDoType;
import com.ll.zzandi.service.ToDoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class ToDoListController {
    private final ToDoListService toDoListService;

    @GetMapping("/list")
    public String findToDoList() {
        return "todo/ToDoListMainAsync";
    }

    @GetMapping("/create")
    @ResponseBody
    public ToDoList createToDo(String content) {
        ToDoListDto.ToDoListRequest toDoListRequest = new ToDoListDto.ToDoListRequest(content);

        return toDoListService.save(toDoListRequest);
    }

    @GetMapping("/update")
    @ResponseBody
    public ToDoList updateToDo(@RequestParam long id, @RequestParam String content) {


        return toDoListService.update(id, content);
    }

    @GetMapping("/change")
    @ResponseBody
    public ToDoList changeToDoType (long id) {
        return toDoListService.changeType(id);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public void deleteToDo (long id) {
        toDoListService.delete(id);
    }

    @GetMapping("/list-data")
    @ResponseBody
    public List<ToDoList> ToDoToJson(@RequestParam(required = false) ToDoType type) {
        return (type == null) ? toDoListService.findAll() : toDoListService.findAllByType(type);
    }

    @GetMapping("/todo-data")
    @ResponseBody
    public ToDoList ToDoToJson(@RequestParam(required = false) long id) {
        return toDoListService.findById(id);
    }
}
