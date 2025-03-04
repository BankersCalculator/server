package com.myZipPlan.server.oauth.userinfo;

import com.myZipPlan.server.community.service.S3Service;
import com.myZipPlan.server.oauth.userInfo.KakaoUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.regex.Pattern;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
public class KakaoUserDetailsServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(KakaoUserDetailsServiceTest.class);

    @InjectMocks
    private KakaoUserDetailsService kakaoUserDetailsService;

    @Mock
    private S3Service s3Service;

    private static final String S3_DIRECTORY_URL = "https://myzipplan-service-storage-dev.s3.ap-northeast-2.amazonaws.com/user-profile/";

    @BeforeEach
    public void setup() {
        lenient().when(s3Service.getS3DirectoryUrl("user-profile/")).thenReturn(S3_DIRECTORY_URL);
    }

    @Test
    public void testGenerateRandomAnimalProfileImageUrl() {
        // Test the URL pattern multiple times to verify randomness
        for (int i = 0; i < 10; i++) {
            String url = kakaoUserDetailsService.generateRandomAnimalProfileImageUrl();
            String expectedPattern = S3_DIRECTORY_URL + "comunity-profile-\\d{2}\\.png";
            Pattern pattern = Pattern.compile(expectedPattern);

            logger.info("Generated URL: " + url);
            assertTrue(pattern.matcher(url).matches(), "The generated URL does not match the expected pattern: " + url);
        }
    }
}
