package com.ll.zzandi.service;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.comment.CommentListDto;
import com.ll.zzandi.enumtype.DeleteStatus;
import com.ll.zzandi.repository.BoardRepository;
import com.ll.zzandi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final BoardRepository boardRepository;

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
                    .step(comment.getStep())
                    .count(comment.getCount())
                    .status(comment.getDeleteStatus())
                    .createdDate(comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")))
                    .build());
        }
        return commentList;
    }

    public Page<CommentListDto> findCommentList(Long boardId, int page) {
        PageRequest pageRequest = PageRequest.of(page, 30, Sort.by(Sort.Direction.ASC, "id"));
        Page<Comment> commentList = commentRepository.findCommentList(pageRequest, boardId);

        return commentList.map(comment -> CommentListDto.builder()
                .commentId(comment.getId())
                .boardId(comment.getBoard().getId())
                .userUUID(comment.getUser().getId())
                .userId(comment.getUser().getUserId())
                .profile(comment.getUser().getUserprofileUrl())
                .writer(comment.getUser().getUserNickname())
                .parentId(comment.getParentId())
                .content(comment.getContent().replace("\r\n", "<br>"))
                .step(comment.getStep())
                .count(comment.getCount())
                .status(comment.getDeleteStatus())
                .createdDate(comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")))
                .build());
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당되는 댓글이 없습니다!"));
    }

    @Transactional
    public Comment createComment(Comment comment, Long boardId, @AuthenticationPrincipal User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("작성할 게시글이 없습니다."));

        Long commentRef = commentRepository.findByNvlRef(boardId);

        if (comment.getId() == null) {
            return commentRepository.save(Comment.builder()
                    .content(comment.getContent())
                    .user(user)
                    .board(board)
                    .ref(commentRef + 1L)
                    .step(0L)
                    .refOrder(0L)
                    .count(0L)
                    .parentId(0L)
                    .deleteStatus(DeleteStatus.EXIST)
                    .build());
        } else {
            Comment parentComment = commentRepository.findById(comment.getId()).orElseThrow(() -> new IllegalArgumentException("가져올 댓글이 없습니다."));
            Long refOrderResult = refOrderAndUpdate(parentComment);

            commentRepository.updateCount(parentComment.getId(), parentComment.getCount());
            return commentRepository.save(Comment.builder()
                    .content(comment.getContent())
                    .user(user)
                    .board(board)
                    .ref(parentComment.getRef())
                    .step(parentComment.getStep() + 1L)
                    .refOrder(refOrderResult)
                    .count(0L)
                    .parentId(comment.getId())
                    .deleteStatus(DeleteStatus.EXIST)
                    .build());
        }
    }

    private Long refOrderAndUpdate(Comment parentComment) {
        Long saveStep = parentComment.getStep() + 1L;
        Long refOrder = parentComment.getRefOrder();
        Long count = parentComment.getCount();
        Long ref = parentComment.getRef();

        Long countSum = commentRepository.findBySumAnswerNum(ref);
        Long maxStep = commentRepository.findByNvlMaxStep(ref);

        if (saveStep < maxStep) {
            return countSum + 1L;
        } else if (saveStep.equals(maxStep)) {
            commentRepository.updateRefOrderPlus(ref, refOrder + count);
            return refOrder + count + 1L;
        } else {
            commentRepository.updateRefOrderPlus(ref, refOrder);
            return refOrder + 1L;
        }
    }

    @Transactional
    public void updateComment(Long commentId, Comment updateParam) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.changeComment(updateParam);
    }

    @Transactional
    public void deleteCommentByCommentId(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        Long childCommentCount = findComment.getCount();

        if(childCommentCount > 0) {
            commentRepository.updateSingleCommentByCommentId(DeleteStatus.DELETE, commentId);
        } else {
            if (findComment.getParentId() > 0) {
                Comment parentComment = commentRepository.findById(findComment.getParentId()).orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
                parentComment.reduceCount();
            }
            commentRepository.deleteSingleCommentByCommentId(commentId);
        }
    }

    @Transactional
    public void deleteCommentByBoardId(Long boardId) {
        commentRepository.deleteCommentByBoardId(boardId);
    }
}