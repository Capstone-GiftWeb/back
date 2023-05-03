package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.RefreshToken;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
}