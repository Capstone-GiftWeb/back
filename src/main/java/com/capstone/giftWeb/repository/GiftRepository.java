package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftRepository extends JpaRepository<Gift,Long> {

    List<Gift> findAllByCategory(int categoryNum);

    List<Gift> findTop100ByOrderByIdDesc(); //테스트 용

    List<Gift> findAllByTitleContains(String search);
}
