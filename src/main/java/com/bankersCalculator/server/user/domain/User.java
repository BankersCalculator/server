package com.bankersCalculator.server.user.domain;

import com.bankersCalculator.server.common.domain.BaseTimeEntity;
import com.bankersCalculator.server.common.enums.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String refreshToken;

    @Column
    private RoleType roleType;


    @Builder
    private User(String oauthProvider, String oauthProviderId, String email, String refreshToken, RoleType roleType) {
        this.oauthProvider = oauthProvider;
        this.oauthProviderId = oauthProviderId;
        this.email = email;
        this.refreshToken = refreshToken;
        this.roleType = roleType;
    }
}
