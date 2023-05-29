package com.capstone.giftWeb.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

public interface GiftSearchRepository {
    public List<String> wordSearchShow(String searchWord);
}
