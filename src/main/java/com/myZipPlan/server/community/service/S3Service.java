package com.myZipPlan.server.community.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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
        // imageUri에서 파일 경로 추출
        String fileKey = imageUri.replace("https://" + bucket + ".s3.amazonaws.com/", "");

        // 버킷 URL에 리전 정보가 포함된 경우 처리
        if (imageUri.contains("s3.")) {
            // 리전이 포함된 URL 처리 (예: s3.ap-northeast-2.amazonaws.com)
            fileKey = imageUri.substring(imageUri.indexOf(".amazonaws.com/") + 15);
        }

        // S3에서 파일 삭제
        amazonS3.deleteObject(bucket, fileKey);
    }
}
