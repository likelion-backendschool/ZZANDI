package com.ll.zzandi.domain;

import com.ll.zzandi.enumtype.DeleteStatus;
import com.ll.zzandi.enumtype.TableType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class File {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "FILE_ORIGIN_NAME")
    private String originalName;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "TABLE_TYPE")
    private TableType tableType;

    @Column(name ="TABLE_ID")
    private Long tableId;

    @Column(name = "FILE_SIZE")
    private Integer fileSize;

    @Column(name="FILE_EXT")
    private String fileExt;

    @Column(name="FILE_STAUTS")
    @Enumerated(value = EnumType.STRING)
    private DeleteStatus fileStatus;

    @Column(name="FILE_URL")
    private String fileUrl;

    @Column(name="DELETE_DATE")
    private LocalDateTime deletedDate;

    @Builder
    public File(String originalName, String fileName, TableType tableType, Long tableId, Integer fileSize, String fileExt, String fileUrl) {
        this.originalName = originalName;
        this.fileName = fileName;
        this.tableType = tableType;
        this.tableId = tableId;
        this.fileSize = fileSize;
        this.fileExt = fileExt;
        this.fileUrl = fileUrl;
        this.fileStatus=DeleteStatus.EXIST;
    }
}
