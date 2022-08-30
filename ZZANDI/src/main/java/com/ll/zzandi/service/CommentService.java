package com.ll.zzandi.service;

import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.dto.comment.CommentCreateDto;
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

    public List<CommentCreateDto> findCommentList(Long boardId) {
        List<Comment> commentListByBoardId = commentRepository.findCommentListByBoardId(boardId);
        List<CommentCreateDto> commentList = new ArrayList<>();

        for (Comment comment : commentListByBoardId) {
            commentList.add(CommentCreateDto.builder()
                    .commentId(comment.getId())
                    .boardId(comment.getBoard().getId())
                    .userUUID(comment.getUser().getId())
                    .userId(comment.getUser().getUserId())
                    .profile(comment.getUser().getUserprofileUrl())
                    .writer(comment.getUser().getUserNickname())
                    .parentId(comment.getParentId())
                    .content(comment.getContent())
                    .status(comment.getDeleteStatus())
                    .createdDate(comment.getCreatedDate()).build());
        }
        return commentList;
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