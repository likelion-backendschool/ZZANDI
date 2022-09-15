package com.ll.zzandi.util.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageUploadService {
    @Value("${cloud.aws.s3.bucket.name}")
    private String bucket;
    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;
    private final AmazonS3Client amazonS3Client;

    public String upload(String saveFileName, MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("user.dir") + saveFileName);
        multipartFile.transferTo(file);
        final TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3Client).build();
        System.out.println(saveFileName);
        final PutObjectRequest request = new PutObjectRequest(bucket, saveFileName, file);
        final Upload upload =  transferManager.upload(request);
        try {
            upload.waitForCompletion();
            return defaultUrl+saveFileName;
        } catch (AmazonClientException | InterruptedException amazonClientException) {
            amazonClientException.printStackTrace();
            return null;
        }finally {
            file.delete();
        }

    }
    // https://zzandi.s3.ap-northeast-2.amazonaws.com/14a4c8d5e8c04b7fbb4fedb3d217d90f.jpeg 이렇게 들어오면 14~~부터 끝까지만 파싱해서 넘긴다.

    public void deleteFile(String fileUrl) {
        try {
            amazonS3Client.deleteObject(bucket,fileUrl.substring(defaultUrl.length()));
            System.out.println(String.format("[%s] deletion complete", fileUrl));
        }catch (AmazonServiceException e) {
            e.printStackTrace();
        }
    }
}
