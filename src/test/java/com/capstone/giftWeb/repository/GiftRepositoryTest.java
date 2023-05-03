package com.capstone.giftWeb.repository;

import com.capstone.giftWeb.domain.Gift;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GiftRepositoryTest {

    @Autowired
    GiftRepository giftRepository;

    @Test
    void findAllByCategory() {
        // Given
        Gift gift1 = new Gift();
        gift1.setId(1L);
        gift1.setTitle("test gift 1");
        gift1.setCategory(1);
        giftRepository.save(gift1);

        Gift gift2 = new Gift();
        gift2.setId(2L);
        gift2.setTitle("test gift 2");
        gift2.setCategory(2);
        giftRepository.save(gift2);

        // When
        List<Gift> result1 = giftRepository.findAllByCategory(1);

        // Then
        assertThat(result1.size()).isEqualTo(1);
        assertThat(result1.get(0).getTitle()).isEqualTo("test gift 1");
        assertThat(result1.get(0).getCategory()).isEqualTo(1);

        List<Gift> result2 = giftRepository.findAllByCategory(2);

        // Then
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result2.get(0).getTitle()).isEqualTo("test gift 2");
        assertThat(result2.get(0).getCategory()).isEqualTo(2);
    }

    @Test
    void findTop100ByOrderByIdDesc() {
        // Given
        for (int i = 1; i <= 150; i++) {
            Gift gift = new Gift();
            gift.setId(Long.valueOf(i));
            gift.setTitle("test gift " + i);
            giftRepository.save(gift);
        }

        // When
        List<Gift> result = giftRepository.findTop100ByOrderByIdDesc();

        // Then
        assertThat(result.size()).isEqualTo(100);
        assertThat(result.get(0).getTitle()).isEqualTo("test gift 150");
        assertThat(result.get(99).getTitle()).isEqualTo("test gift 51");
    }

    @Test
    void findAllByTitleContains() {
        // Given
        Gift gift1 = new Gift();
        gift1.setId(1L);
        gift1.setTitle("test gift 1");
        gift1.setCategory(1);
        giftRepository.save(gift1);

        Gift gift2 = new Gift();
        gift2.setId(2L);
        gift2.setTitle("test gift 2");
        gift2.setCategory(2);
        giftRepository.save(gift2);

        Gift gift3 = new Gift();
        gift3.setId(3L);
        gift3.setTitle("gift 3"); // not contain 'test'
        gift3.setCategory(3);
        giftRepository.save(gift3);

        // When
        List<Gift> result = giftRepository.findAllByTitleContains("test");

        // Then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getTitle()).isEqualTo("test gift 1");
        assertThat(result.get(1).getTitle()).isEqualTo("test gift 2");
    }
}