package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.Gift;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.capstone.giftWeb.domain.QGift.gift;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GiftSearchRepositoryTest {

    private GiftSearchRepository giftSearchRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp(){
        JPAQueryFactory jpaQueryFactory= new JPAQueryFactory(entityManager);
        giftSearchRepository=new GiftSearchRepositoryImpl(jpaQueryFactory);
    }

    @Test
    void testWordSearchShow() {
        // given
        String searchWord = "gift";

        Gift gift1 = new Gift();
        gift1.setId(1L);
        gift1.setTitle("1gift1");
        Gift gift2 = new Gift();
        gift2.setId(2L);
        gift2.setTitle("2gift2");
        Gift gift3 = new Gift();
        gift3.setId(3L);
        gift3.setTitle("3gift3");
        Gift gift4 = new Gift();
        gift4.setId(4L);
        gift4.setTitle("4item4");
        entityManager.persist(gift1);
        entityManager.persist(gift2);
        entityManager.persist(gift3);
        entityManager.persist(gift4);

        // when
        List<String> result = giftSearchRepository.wordSearchShow(searchWord);


        // then
        assertEquals(result.size(),3);
        for (String title : result) {
            assertThat(title).containsIgnoringCase(searchWord);
        }
    }
}
