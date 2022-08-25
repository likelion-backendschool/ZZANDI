package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.board.id = :id")
    List<Comment> findCommentByBoardId(@Param("id") Long boardId);

    @Modifying
    @Query("delete from Comment c where c.board.id = :boardId")
    void deleteCommentByBoardId(@Param("boardId") Long boardId);
}
