package com.bankersCalculator.server.user.repository;

import com.bankersCalculator.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthProviderAndEmail(String provider, String email);

}
