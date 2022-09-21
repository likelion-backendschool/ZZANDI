package com.ll.zzandi.repository;

import com.ll.zzandi.domain.File;
import com.ll.zzandi.dto.board.BoardFileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {
    File findByTableId(Long userUUID);

    @Query(value = "select * from file where table_id = :boardId and table_type ='BOARD'", nativeQuery = true)
    List<File> findFileByBoardId(@Param("boardId") Long boardId);
}
