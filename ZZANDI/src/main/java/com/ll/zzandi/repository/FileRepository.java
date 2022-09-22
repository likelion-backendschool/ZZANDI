package com.ll.zzandi.repository;

import com.ll.zzandi.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {
    File findByTableId(Long userUUID);

    @Query(value = "select * from file where table_id = :boardId and table_type ='BOARD'", nativeQuery = true)
    List<File> findFileByBoardId(@Param("boardId") Long boardId);

    @Modifying
    @Query(value = "update file set file_stauts = 'DELETE' where id = :fileId", nativeQuery = true)
    void updateFileStatus(@Param("fileId") Long fileId);
}
