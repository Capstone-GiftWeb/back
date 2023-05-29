package com.capstone.giftWeb.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.capstone.giftWeb.domain.QGift.gift;

@Repository
@RequiredArgsConstructor
public class GiftSearchRepositoryImpl implements GiftSearchRepository{


    private final JPAQueryFactory jpaQueryFactory;
    public List<String> wordSearchShow(String searchWord) {
        return jpaQueryFactory.select(gift.title)
                .from(gift)
                .where(gift.title.likeIgnoreCase("%" + searchWord + "%"))
                .distinct()
                .limit(5)
                .fetch();
    }

}
