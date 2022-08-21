package com.ll.zzandi.util.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3 {

    @Value("${cloud.aws.s3.bucket.name}")
    public String bucket;

    private final String dir="/test";

    private final AmazonS3Client amazonS3Client;


    public String uploadImg(MultipartFile multipartFile) throws IOException {
       String[] name=multipartFile.getOriginalFilename().split("\\\\");
        System.out.println("uploadImg 사이즈 :"+multipartFile.getSize());
        final String ext = name[2].substring(name[2].lastIndexOf('.'));
        uploadImgToS3(multipartFile,name[2],ext);
        return "good";
    }

    public String uploadImgToS3(MultipartFile multipartFile, String Filename, String ext)
            throws IllegalStateException, IOException {
        String url;
        try {
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            System.out.println("높이는 : "+image.getHeight());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            log.info("확장자는 :{}", ext.substring(1));
            ImageIO.write(image, ext.substring(1), os);
            byte[] bytes = os.toByteArray();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);
            objectMetadata.setContentType(ext.substring(1));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//            final TransferManager transferManager = new TransferManager(this.amazonS3Client);
            TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3Client).build();

            final PutObjectRequest request = new PutObjectRequest(bucket, Filename, multipartFile.getInputStream(), objectMetadata);
            final Upload upload = transferManager.upload(request);
            upload.waitForCompletion();
        } catch (AmazonServiceException | InterruptedException e) {
            e.printStackTrace();
        }
        return "good";
    }
    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}