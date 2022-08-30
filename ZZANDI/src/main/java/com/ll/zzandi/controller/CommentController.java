package com.ll.zzandi.controller;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.comment.CommentCreateDto;
import com.ll.zzandi.enumtype.DeleteStatus;
import com.ll.zzandi.service.BoardService;
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
    private final BoardService boardService;

    @GetMapping("/list/{boardId}")
    @ResponseBody
    public Result<List<CommentCreateDto>> findCommentList(@PathVariable Long boardId) {
        List<CommentCreateDto> responses = commentService.findCommentList(boardId);
        return new Result<>(responses.size(), responses);
    }

    @PostMapping("/create/{boardId}")
    @ResponseBody
    public void createComment(@AuthenticationPrincipal User user, @PathVariable Long boardId, @RequestBody Comment data) {
        Board board = boardService.findByBoardId(boardId);

        Comment comment = new Comment(board, user, 0L, data.getContent(), DeleteStatus.EXIST);
        commentService.createComment(comment);
    }

    // 컬렉션 데이터를 한 번 감싸서 반환하기 위한 클래스
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T comment;
    }

}
