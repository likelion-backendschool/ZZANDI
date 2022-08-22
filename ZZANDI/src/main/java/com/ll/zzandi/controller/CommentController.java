package com.ll.zzandi.controller;

import com.ll.zzandi.dto.CommentDto.Response;
import com.ll.zzandi.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list/{boardId}")
    @ResponseBody
    public Result<List<Response>> commentList(@PathVariable Long boardId) {
        List<Response> responses = commentService.commentList(boardId);
        return new Result<>(responses.size(), responses);
    }

    // 컬렉션 데이터를 한 번 감싸서 반환하기 위한 클래스
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T comment;
    }

}
