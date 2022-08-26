package com.ll.zzandi.service;

import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.dto.CommentDto.Response;
import com.ll.zzandi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Response> findCommentList(Long boardId) {
        List<Comment> list = commentRepository.findCommentListByBoardId(boardId);
        List<Response> responseList = new ArrayList<>();
        for (Comment comment : list) {
            responseList.add(new Response(comment.getId(), comment.getBoard().getId(), comment.getUser().getId(), comment.getUser().getUserNickname(),
                    comment.getUser().getUserprofileUrl(), comment.getParentId(), comment.getContent(), comment.getDeleteStatus(), comment.getCreatedDate()));
        }
        return responseList;
    }

    @Transactional
    public Long createComment(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    public void deleteComment(Long boardId) {
        commentRepository.deleteCommentByBoardId(boardId);
    }
}