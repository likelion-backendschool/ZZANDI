package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.comment.CommentListDto;
import com.ll.zzandi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list/{boardId}/{page}")
    public Page<CommentListDto> findCommentList(@PathVariable Long boardId, @PathVariable int page) {
        return commentService.findCommentList(boardId, page);
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

}