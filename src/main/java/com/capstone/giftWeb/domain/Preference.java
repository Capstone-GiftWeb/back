package com.capstone.giftWeb.domain;

import lombok.*;
import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Preference {

    @Id
    @Column(name = "member_id")
    private Long userId;

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "pref_score")
    private Long prefScore;

    @Column(name = "likes")
    private Long likes;

    @Column(name = "clicks")
    private Long clicks;

    @Builder
    public Preference(Long userId, Long categoryId, Long prefScore, Long likes, Long clicks) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.prefScore = prefScore;
        this.likes = likes;
        this.clicks = clicks;
    }
}
