package com.capstone.giftWeb.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
public class Recommendation {

    @Id
    @Column(name = "member_id")
    private Long userId;

    private Long categoryId;

    @OneToMany
    private List<Recommendation> recommendationList;

    @Builder
    public Recommendation(Long userId, List<Recommendation> recommendationList) {
        this.userId = userId;
        this.recommendationList = recommendationList;
    }
}
