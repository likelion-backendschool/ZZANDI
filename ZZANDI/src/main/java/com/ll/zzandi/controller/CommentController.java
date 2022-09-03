package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.comment.CommentListDto;
import com.ll.zzandi.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list/{boardId}")
    @ResponseBody
    public Result<List<CommentListDto>> findCommentList(@PathVariable Long boardId) {
        List<CommentListDto> responses = commentService.findCommentList(boardId);
        return new Result<>(responses.size(), responses);
    }

    @PostMapping("/create/{boardId}")
    @ResponseBody
    public void createComment(@AuthenticationPrincipal User user, @PathVariable Long boardId, @RequestBody Comment comment) {
        commentService.createComment(comment, boardId, user);
    }

    @PostMapping("/update/{commentId}")
    @ResponseBody
    public void updateComment(@PathVariable Long commentId, @RequestBody Comment comment) {
        commentService.updateComment(commentId, comment);
    }

    @PostMapping("/delete/{commentId}")
    @ResponseBody
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteCommentByCommentId(commentId);
    }

    // 컬렉션 데이터를 한 번 감싸서 반환하기 위한 클래스
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T comment;
    }
}