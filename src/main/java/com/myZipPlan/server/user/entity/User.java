package com.myZipPlan.server.user.entity;

import com.myZipPlan.server.common.domain.BaseTimeEntity;
import com.myZipPlan.server.common.enums.ABTestType;
import com.myZipPlan.server.common.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column
    private String name;
    @Column
    private String provider;
    @Column
    private String providerId;
    @Column
    private String email;
    @Column
    private String profileImageUrl;
    @Column
    private RoleType roleType;
    @Column
    private String animalProfileImageUrl;
    @Column
    private String animalUserName;
    @Column
    private ABTestType abTestType;

    public static User create(String provider, String providerId, String nickname, String email,
                              String thumbnailImage, RoleType roleType, String animalProfileImageUrl) {

        String animalName = generateRandomAnimalUsername();

        return User.builder()
            .provider(provider)
            .providerId(providerId)
            .name(nickname)
            .email(email)
            .profileImageUrl(thumbnailImage)
            .roleType(roleType)
            .animalProfileImageUrl(animalProfileImageUrl)
            .animalUserName(animalName)
            .build();
    }

    public static User createGuestUser(ABTestType abTestType) {

        String animalName = generateRandomAnimalUsername();

        return User.builder()
            .provider("GUEST")
            .providerId(UUID.randomUUID().toString())
            .name("guest_" + System.currentTimeMillis())
            .roleType(RoleType.GUEST)
            .animalUserName(animalName)
            .abTestType(abTestType)
            .build();
    }

    public void setABTestType(ABTestType abTestType) {
        this.abTestType = abTestType;
    }

    private static String generateRandomAnimalUsername() {

        List<String> ANIMAL_USERNAME_ADJECTIVES = List.of("포효하는", "눈썰매타는", "재테크천재", "독서하는", "춤추는", "밈잘알", "행복한", "느긋한", "용감한", "우아한", "청약노리는", "건물주꿈꾸는", "임대사업하는", "월세탈출하는", "아파트분양받는", "분산투자왕", "저축왕", "안전자산추구하는", "거시경제분석하는");
        List<String> ANIMAL_USERNAME_ANIMALS = List.of("쿼카", "코끼리", "비버", "개구리", "호랑이", "펭귄", "다람쥐", "고양이", "여우", "사슴", "알파카", "사막여우", "바다표범", "돌고래", "치타", "늑대", "미어캣");

        Random random = new Random();
        String adjective = ANIMAL_USERNAME_ADJECTIVES.get(random.nextInt(ANIMAL_USERNAME_ADJECTIVES.size()));
        String animal = ANIMAL_USERNAME_ANIMALS.get(random.nextInt(ANIMAL_USERNAME_ANIMALS.size()));
        return adjective + " " + animal;
    }
}
