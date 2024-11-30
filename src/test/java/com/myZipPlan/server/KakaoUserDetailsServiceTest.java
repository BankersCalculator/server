package com.myZipPlan.server;

import com.myZipPlan.server.oauth.userInfo.KakaoUserDetailsService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.regex.Pattern;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
public class KakaoUserDetailsServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(KakaoUserDetailsServiceTest.class);

    @InjectMocks
    private KakaoUserDetailsService kakaoUserDetailsService;

    @Test
    public void testGenerateRandomAnimalProfileImageUrl() {
        // Test the URL pattern multiple times to verify randomness
        for (int i = 0; i < 10; i++) {
            String url = kakaoUserDetailsService.generateRandomAnimalProfileImageUrl();
            String expectedPattern = "https://myzipplan-service-storage-dev\\.s3\\.ap-northeast-2\\.amazonaws\\.com/user-profile/comunity-profile-\\d{2}\\.png";
            Pattern pattern = Pattern.compile(expectedPattern);

            logger.info("Generated URL: " + url);
            assertTrue(pattern.matcher(url).matches(), "The generated URL does not match the expected pattern: " + url);
        }
    }

    @Test
    public void testGenerateRandomAnimalUsername() {
        // Test the username pattern multiple times to verify randomness
        for (int i = 0; i < 10; i++) {
            String nickname = kakaoUserDetailsService.generateRandomAnimalUsername();
            String expectedPattern = "[가-힣]+ [가-힣]+";
            Pattern pattern = Pattern.compile(expectedPattern);

            logger.info("Generated Nickname: " + nickname);
            assertTrue(pattern.matcher(nickname).matches(), "The generated nickname does not match the expected pattern: " + nickname);
        }
    }
}
