package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Board;
import com.ll.zzandi.enumtype.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b where b.study.id = :studyId and b.category like %:category%")
    Page<Board> findBoardList(Pageable pageable, @Param("studyId") Long studyId, @Param("category") String category);

    @Query("select b from Board b where b.study.id = :studyId")
    List<Board> findBoardListByStudyId(@Param("studyId") Long studyId);

    @Query(value = "select * from board b join study s on b.study_id = s.study_id where b.study_id = 1 and b.board_id < :boardId order by b.board_id desc limit 1", nativeQuery = true)
    Optional<Board> findPrevBoard(@Param("boardId") Long boardId);

    @Query(value = "select * from board b join study s on b.study_id = s.study_id where b.study_id = 1 and b.board_id > :boardId order by b.board_id limit 1", nativeQuery = true)
    Optional<Board> findNextBoard(@Param("boardId") Long boardId);

    @Modifying
    @Query("delete from Board b where b.study.id = :studyId")
    void deleteBoardByStudyId(@Param("studyId") Long studyId);

    @Modifying
    @Query("update Board b set b.views = b.views + 1 where b.id = :id")
    void updateView(@Param("id") Long id);
}
