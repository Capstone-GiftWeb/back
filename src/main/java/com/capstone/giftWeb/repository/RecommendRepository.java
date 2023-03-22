package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Preference, Long> {

}
