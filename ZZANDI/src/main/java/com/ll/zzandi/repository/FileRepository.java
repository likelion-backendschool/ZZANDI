package com.ll.zzandi.repository;

import com.ll.zzandi.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;



public interface FileRepository extends JpaRepository<File,Long> {
    File findByTableId(Long userUUID);
}
