package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.sql.DataSource;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommendation, Long> {
    Optional<Recommendation> findById(Long id);
}
