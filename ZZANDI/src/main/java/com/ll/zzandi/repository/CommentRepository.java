package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Comment;
import com.ll.zzandi.enumtype.DeleteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.board.id = :boardId order by c.ref, c.refOrder")
    Page<Comment> findCommentList(Pageable pageable, @Param("boardId") Long boardId);

    @Query(value = "select c from Comment c where c.id = :parentId")
    Comment findCommentByParentId(@Param("parentId") Long parentId);

    @Query(value = "select nvl(max(cm_ref), 0) from comment c where c.board_id = :boardId", nativeQuery = true)
    Long findByNvlRef(@Param("boardId") Long boardId);

    @Query(value = "select sum(cm_count) from comment where board_id = :boardId and cm_ref = :ref", nativeQuery = true)
    Long findBySumAnswerNum(@Param("ref") Long ref, @Param("boardId") Long boardId);

    @Query(value = "select max(cm_step) from comment where board_id = :boardId and cm_ref = :ref", nativeQuery = true)
    Long findByNvlMaxStep(@Param("ref") Long ref, @Param("boardId") Long boardId);

    @Modifying
    @Query(value = "update comment set cm_ref_order = cm_ref_order + 1 where board_id = :boardId and cm_ref = :ref and cm_ref_order > :refOrder", nativeQuery = true)
    void updateRefOrderPlus(@Param("ref") Long ref, @Param("refOrder") long refOrder, @Param("boardId") Long boardId);

    @Modifying
    @Query(value = "update comment set cm_count = :count + 1 where cm_id = :commentId", nativeQuery = true)
    void updateCount(@Param("commentId") Long commentId, @Param("count") Long count);

    @Modifying
    @Query("delete from Comment c where c.id = :commentId")
    void deleteSingleCommentByCommentId(@Param("commentId") Long commentId);

    @Modifying
    @Query("update Comment c set c.content = '[삭제된 댓글입니다.]' , c.deleteStatus = :status, c.deletedDate = current_timestamp where c.id = :commentId")
    void updateSingleCommentByCommentId(@Param("status") DeleteStatus deleteStatus, @Param("commentId") Long commentId);

    @Modifying
    @Query("delete from Comment c where c.board.id = :boardId")
    void deleteCommentByBoardId(@Param("boardId") Long boardId);
}
