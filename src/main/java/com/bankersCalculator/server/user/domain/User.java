package com.bankersCalculator.server.user.domain;

import com.bankersCalculator.server.common.domain.BaseTimeEntity;
import com.bankersCalculator.server.common.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;


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
    private String oauthProvider;

    @Column
    private String oauthProviderId;

    @Column
    private String email;

    @Column
    private RoleType roleType;

    public static User create(String oauthProvider, String oauthProviderId, String email, String refreshToken, RoleType roleType) {
        return User.builder()
            .oauthProvider(oauthProvider)
            .oauthProviderId(oauthProviderId)
            .email(email)
            .roleType(roleType)
            .build();
    }

    public static User createTempUser(String tempUserId) {
        return User.builder()
            .oauthProvider("temp")
            .oauthProviderId(null)
            .email(tempUserId)
            .roleType(RoleType.USER)
            .build();
    }
}
