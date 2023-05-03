package com.capstone.giftWeb.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@IdClass(CategoryPref.class)
public class CategoryPref implements Serializable {
    @Id
    @Column(name = "member_id")
    private Long userId;

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "prev_score")
    private Float prevScore;

    @Column(name = "do_like")
    @ColumnDefault("false")
    private Boolean doLike;

    @Column(name = "clicks")
    private Long clicks;

    @Column(name = "pref_score")
    private Float prefScore;

    @Builder
    public CategoryPref(Long userId, Long categoryId, Float prevScore, Boolean doLike, Long clicks, Float prefScore) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.prevScore = prevScore;
        this.doLike = doLike;
        this.clicks = clicks;
        this.prefScore = prefScore;
    }
}
