package com.capstone.giftWeb.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@NoArgsConstructor
public class GiftPref implements Serializable {
    @Id
    @Column(name = "member_id")
    private Long userId;

    @Id
    @Column(name = "gift_id")
    private Long giftId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "prev_score")
    private Float prevScore;

    @Column(name = "do_like") // like : +1
    @ColumnDefault("0")
    private Long doLike;

    @Column(name = "clicks") // click : +0.1
    private Long clicks;

    @Column(name = "pref_score")
    private Float prefScore;

    @Builder
    public GiftPref(Long userId, Long giftId, Float prevScore, Long doLike, Long clicks, Float prefScore) {
        this.userId = userId;
        this.giftId = giftId;
        this.prevScore = prevScore;
        this.doLike = doLike;
        this.clicks = clicks;
        this.prefScore = prefScore;
    }
}
