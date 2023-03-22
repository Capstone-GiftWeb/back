package com.capstone.giftWeb.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
public class Preference implements Serializable {

    @Id
    @Column(name = "member_id")
    private Long userId;

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "prev_score")
    private Long prevScore;

    @Column(name = "like")
    @ColumnDefault("false")
    private Boolean like;

    @Column(name = "clicks")
    private Long clicks;

    @Column(name = "pref_score")
    private Long prefScore;

    @Builder
    public Preference(Long userId, Long categoryId, Long prevScore, Boolean like, Long clicks, Long prefScore) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.prevScore = prevScore;
        this.like = like;
        this.clicks = clicks;
        this.prefScore = prefScore;
    }
}
