package com.ll.zzandi.domain;

import com.ll.zzandi.enumtype.DeleteStatus;
import com.ll.zzandi.enumtype.TableType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql="UPDATE file SET DELETE_DATE = NOW() where id=?")
@Where(clause="DELETE_DATE is NULL")
public class File extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "FILE_ORIGIN_NAME")
    private String originalName;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "TABLE_TYPE")
    @Enumerated(value = EnumType.STRING)
    private TableType tableType;

    @Column(name ="TABLE_ID")
    private Long tableId;

    @Column(name = "FILE_SIZE")
    private Long fileSize;

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
    public File(String originalName, String fileName, TableType tableType, Long tableId, Long fileSize, String fileExt, String fileUrl) {
        this.originalName = originalName;
        this.fileName = fileName;
        this.tableType = tableType;
        this.tableId = tableId;
        this.fileSize = fileSize;
        this.fileExt = fileExt;
        this.fileUrl = fileUrl;
        this.fileStatus=DeleteStatus.EXIST;
    }

    public void deleteFileStatus(){
        this.fileStatus=DeleteStatus.DELETE;
    }
}
