package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Study;
import com.ll.zzandi.domain.ToDoList;
import com.ll.zzandi.dto.ToDoListDto;
import com.ll.zzandi.enumtype.Type;
import com.ll.zzandi.service.ToDoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class ToDoListController {
    private final ToDoListService toDoListService;

    @GetMapping()
    public String showToDoMain(Model model, ToDoListDto.ToDoListRequest toDoListRequest) {
        List<ToDoList> toDoList = toDoListService.findAll();
        model.addAttribute("toDoList", toDoList);
        return "todo/ToDoListMain";
    }

    @PostMapping("/add")
    public String addTodo (Model model, @Valid ToDoListDto.ToDoListRequest toDoListRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            List<ToDoList> toDoList = toDoListService.findAll();
            model.addAttribute("toDoList", toDoList);
            return "todo/ToDoListMain";
        }

        toDoListService.save(toDoListRequest);

        return "redirect:/todo";
    }

    @GetMapping("/change")
    public String changeType (long id) {
        toDoListService.changeType(id);

        return "redirect:/todo";
    }
}
