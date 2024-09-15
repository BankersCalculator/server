package com.myZipPlan.server.community.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);


    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 파일 URL 반환
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // 파일 삭제 메서드 (imageUri 사용)
    public void deleteFileByImageUri(String imageUri) {

        logger.info("Deleting file from S3: {}", imageUri); // 이미지 삭제 전 로그

        // imageUri에서 파일 경로 추출
        String fileKey = imageUri.replace("https://" + bucket + ".s3.amazonaws.com/", "");

        // 버킷 URL에 리전 정보가 포함된 경우 처리
        if (imageUri.contains("s3.")) {
            // 리전이 포함된 URL 처리 (예: s3.ap-northeast-2.amazonaws.com)
            fileKey = imageUri.substring(imageUri.indexOf(".amazonaws.com/") + 15);
        }

        // URL 디코딩
        String decodedFileKey = URLDecoder.decode(fileKey, StandardCharsets.UTF_8);
        logger.info("Decoded file key: {}", decodedFileKey); // 디코딩된 파일 경로 로그


        // S3에서 파일 삭제
        amazonS3.deleteObject(bucket, decodedFileKey);
        logger.info("File deleted from S3: {}", decodedFileKey); // 삭제 완료 로그

    }
}
