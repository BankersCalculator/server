package com.myZipPlan.server.user.entity;

import com.myZipPlan.server.common.domain.BaseTimeEntity;
import com.myZipPlan.server.common.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

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

    public static User create(String provider, String providerId, String nickname, String email,
                              String thumbnailImage, RoleType roleType, String animalProfileImageUrl, String animalUserName) {
        return User.builder()
            .provider(provider)
            .providerId(providerId)
            .name(nickname)
            .email(email)
            .profileImageUrl(thumbnailImage)
            .roleType(roleType)
            .animalProfileImageUrl(animalProfileImageUrl)
            .animalUserName(animalUserName)
            .build();
    }

    public static User createGuestUser() {
        return User.builder()
            .provider("GUEST")
            .providerId(UUID.randomUUID().toString())
            .name("guest_" + System.currentTimeMillis())
            .roleType(RoleType.GUEST)
            .build();
    }
}
