package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.CategoryPref;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<CategoryPref, Long> {

}
