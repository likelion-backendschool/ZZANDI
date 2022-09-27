package com.ll.zzandi.repository;

import com.ll.zzandi.domain.File;
import com.ll.zzandi.enumtype.DeleteStatus;
import com.ll.zzandi.enumtype.TableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {

    File findFileByFileStatusAndTableIdAndTableType(DeleteStatus deleteStatus, Long id,
        TableType tableType);

    @Query(value = "select * from file where table_id = :boardId and table_type ='BOARD'", nativeQuery = true)
    List<File> findFileByBoardId(@Param("boardId") Long boardId);

    @Query("select count(f) from File f where f.tableId = :boardId and f.tableType = 'BOARD' and f.fileStatus = 'EXIST'")
    int findExistFileCount(@Param("boardId") Long boardId);

    @Modifying
    @Query(value = "update file set file_stauts = 'DELETE' where id = :fileId", nativeQuery = true)
    void updateFileStatus(@Param("fileId") Long fileId);

    @Modifying
    @Query(value = "UPDATE FILE SET file_stauts = 'EXIST', delete_date = NULL WHERE table_id = :boardId AND table_type = 'BOARD'", nativeQuery = true)
    void updateFileStatusExist(@Param("boardId") Long boardId);
}
