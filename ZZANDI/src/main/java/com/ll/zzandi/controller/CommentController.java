package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.CommentDto.Response;
import com.ll.zzandi.enumtype.DeleteStatus;
import com.ll.zzandi.service.BoardService;
import com.ll.zzandi.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;

    @GetMapping("/list/{boardId}")
    @ResponseBody
    public Result<List<Response>> commentList(@PathVariable Long boardId) {
        List<Response> responses = commentService.commentList(boardId);
        return new Result<>(responses.size(), responses);
    }

    @PostMapping("/write/{boardId}")
    @ResponseBody
    public void commentWrite(@PathVariable Long boardId, @RequestBody Comment data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal(); // 현재 로그인 한 유저 정보

        Board board = boardService.findById(boardId);
        Comment comment = new Comment(board, user, 0L, data.getContent(), DeleteStatus.EXIST);
        commentService.save(comment);
    }

    // 컬렉션 데이터를 한 번 감싸서 반환하기 위한 클래스
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T comment;
    }

}
