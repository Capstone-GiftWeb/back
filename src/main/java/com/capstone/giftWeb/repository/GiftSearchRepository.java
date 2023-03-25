package com.capstone.giftWeb.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.capstone.giftWeb.domain.QGift.gift;

@Repository
public class GiftSearchRepository {


    private final JPAQueryFactory jpaQueryFactory;

    public GiftSearchRepository(JPAQueryFactory jpaQueryFactory){
        this.jpaQueryFactory=jpaQueryFactory;
    }
    public List<String> wordSearchShow(String searchWord) {
        List<String> result = jpaQueryFactory.select(gift.title)
                .from(gift)
                .where(gift.title.likeIgnoreCase("%" + searchWord + "%"))
                .distinct()
                .limit(5)
                .fetch();
        return result;
    }
}
