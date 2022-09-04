package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.comment.CommentListDto;
import com.ll.zzandi.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list/{boardId}")
    public Result<List<CommentListDto>> findCommentList(@PathVariable Long boardId) {
        List<CommentListDto> responses = commentService.findCommentList(boardId);
        return new Result<>((long) responses.size(), responses);
    }

    @GetMapping("/list-json/{boardId}/{page}")
    public Page<CommentListDto> findCommentListJSON(@PathVariable Long boardId, @PathVariable int page) {
        return commentService.findCommentList2(boardId, page);
    }

    @PostMapping("/create/{boardId}")
    public void createComment(@AuthenticationPrincipal User user, @PathVariable Long boardId, @RequestBody Comment comment) {
        commentService.createComment(comment, boardId, user);
    }

    @PostMapping("/update/{commentId}")
    public void updateComment(@PathVariable Long commentId, @RequestBody Comment comment) {
        commentService.updateComment(commentId, comment);
    }

    @PostMapping("/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteCommentByCommentId(commentId);
    }

    // 컬렉션 데이터를 한 번 감싸서 반환하기 위한 클래스
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private Long count;
        private T comment;
    }
}