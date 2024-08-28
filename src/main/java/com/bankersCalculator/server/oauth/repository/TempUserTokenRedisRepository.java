package com.bankersCalculator.server.oauth.repository;

import com.bankersCalculator.server.oauth.token.TempUserToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempUserTokenRedisRepository extends CrudRepository<TempUserToken, Long> {

    TempUserToken findByTempUserId(String tempUserId);
}
