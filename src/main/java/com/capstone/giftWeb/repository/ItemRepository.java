package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findAllByCategory(int categoryNum);

    List<Item> findTop100ByOrderByIdDesc(); //테스트 용
}
