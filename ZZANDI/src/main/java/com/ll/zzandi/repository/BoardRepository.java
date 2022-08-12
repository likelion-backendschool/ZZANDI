package com.ll.zzandi.repository;

import com.ll.zzandi.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findBoardBy(Pageable pageable);

    @Modifying
    @Query("update Board b set b.views = b.views + 1 where b.id = :id")
    int updateView(@Param("id") Long id);
}
