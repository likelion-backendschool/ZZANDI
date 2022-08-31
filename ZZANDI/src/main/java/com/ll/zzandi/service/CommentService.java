package com.ll.zzandi.service;

import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.dto.comment.CommentListDto;
import com.ll.zzandi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentListDto> findCommentList(Long boardId) {
        List<Comment> commentListByBoardId = commentRepository.findCommentListByBoardId(boardId);
        List<CommentListDto> commentList = new ArrayList<>();

        for (Comment comment : commentListByBoardId) {
            String content = comment.getContent().replace("\r\n", "<br>");
            commentList.add(CommentListDto.builder()
                    .commentId(comment.getId())
                    .boardId(comment.getBoard().getId())
                    .userUUID(comment.getUser().getId())
                    .userId(comment.getUser().getUserId())
                    .profile(comment.getUser().getUserprofileUrl())
                    .writer(comment.getUser().getUserNickname())
                    .parentId(comment.getParentId())
                    .content(content)
                    .status(comment.getDeleteStatus())
                    .createdDate(comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")))
                    .build());
        }
        return commentList;
    }

    @Transactional
    public Long createComment(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    public void updateComment(Long commentId, Comment updateParam) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.changeComment(updateParam);
    }

    @Transactional
    public void deleteComment(Long boardId) {
        commentRepository.deleteCommentByBoardId(boardId);
    }
}