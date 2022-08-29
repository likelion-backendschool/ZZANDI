package com.ll.zzandi.controller;

import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.ToDoListDto;
import com.ll.zzandi.enumtype.ToDoType;
import com.ll.zzandi.service.ToDoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;

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
    public ToDoList createToDo(@AuthenticationPrincipal User user, String content) {
        ToDoListDto.ToDoListRequest toDoListRequest = new ToDoListDto.ToDoListRequest(content, user);

        return toDoListService.save(toDoListRequest);
    }

    @GetMapping("/update")
    @ResponseBody
    public ToDoList updateToDo(@RequestParam long id, @RequestParam String content, @AuthenticationPrincipal User user) {
        ToDoList toDoById = toDoListService.findById(id);

        if (! toDoById.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return toDoListService.update(id, content);
    }

    @GetMapping("/change")
    @ResponseBody
    public ToDoList changeToDoType (@RequestParam long id, @AuthenticationPrincipal User user) {
        ToDoList toDoById = toDoListService.findById(id);

        if (! toDoById.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return toDoListService.changeType(id);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public void deleteToDo (@RequestParam long id, @AuthenticationPrincipal User user) {
        ToDoList toDoById = toDoListService.findById(id);

        if (! toDoById.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        toDoListService.delete(id);
    }

    @GetMapping("/list-data")
    @ResponseBody
    public List<ToDoList> ToDoToJson(@RequestParam(required = false) ToDoType type, @AuthenticationPrincipal User user) {
        return (type == null) ? toDoListService.findAllByUser(user) : toDoListService.findAllByUserAndType(user, type);
    }

    @GetMapping("/todo-data")
    @ResponseBody
    public ToDoList ToDoToJson(@RequestParam(required = false) long id, @AuthenticationPrincipal User user) {
        ToDoList toDoById = toDoListService.findById(id);

        if (! toDoById.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return toDoById;
    }
}
