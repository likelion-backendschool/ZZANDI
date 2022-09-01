package com.ll.zzandi.service;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.domain.User;
import com.ll.zzandi.dto.comment.CommentListDto;
import com.ll.zzandi.enumtype.DeleteStatus;
import com.ll.zzandi.repository.BoardRepository;
import com.ll.zzandi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
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
                    .status(comment.getDeleteStatus())
                    .createdDate(comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")))
                    .build());
        }
        return commentList;
    }

    @Transactional
    public Comment createComment(Comment comment, Long boardId, @AuthenticationPrincipal User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("작성할 게시글이 없습니다."));

        // 댓글 그룹번호: NULL이면 0, NULL이 아니면 최대값을 가져옴
        Long commentRef = commentRepository.findByNvlRef(boardId);

        // commentId가 null이면 댓글 저장, 아니면 대댓글 저장
        if (comment.getId() == null) { // 댓글 저장
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
        } else { // 대댓글
            //부모 댓글 데이터
            Comment parentComment = commentRepository.findById(comment.getId()).orElseThrow(() -> new IllegalArgumentException("가져올 댓글이 없습니다."));
            Long refOrderResult = refOrderAndUpdate(parentComment);

            // null이면 대댓글 작성 오류
            if (refOrderResult == null) {
                return null;
            }

            Comment saveReply = commentRepository.save(Comment.builder()
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

            commentRepository.updateCount(parentComment.getId(), parentComment.getCount());
            return saveReply;
        }
    }

    private Long refOrderAndUpdate(Comment parentComment) {
        Long saveStep = parentComment.getStep() + 1L;
        Long refOrder = parentComment.getRefOrder();
        Long count = parentComment.getCount();
        Long ref = parentComment.getRef();

        // 부모 댓글 그룹의 자식 댓글의 개수
        Long countSum = commentRepository.findBySumAnswerNum(ref);
        //부모 댓글그룹의 최댓값 step
        Long maxStep = commentRepository.findByNvlMaxStep(ref);

        //저장할 대댓글 step과 그룹내의 최댓값 step의 조건 비교
        /*
        step + 1 < 그룹리스트에서 max step값  AnswerNum sum + 1 * NO UPDATE
        step + 1 = 그룹리스트에서 max step값  refOrder + AnswerNum + 1 * UPDATE
        step + 1 > 그룹리스트에서 max step값  refOrder + 1 * UPDATE
        */
        if (saveStep < maxStep) {
            return countSum + 1L;
        } else if (saveStep == maxStep) {
            commentRepository.updateRefOrderPlus(ref, refOrder + count);
            //UPDATE BOARD_COMMENTS SET refOrder = refOrder + 1 WHERE ref = ?1 AND refOrder > ?2
            return refOrder + count + 1L;
        } else if (saveStep > maxStep) {
            commentRepository.updateRefOrderPlus(ref, refOrder);
            //UPDATE BOARD_COMMENTS SET refOrder = refOrder + 1 WHERE ref = ?1 AND refOrder > ?2
            return refOrder + 1L;
        }
        return null;
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