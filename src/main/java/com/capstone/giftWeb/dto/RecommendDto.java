package com.capstone.giftWeb.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.List;

@Data
@Getter
@Setter
public class RecommendDto {
    private Long userId;

    private List<RecommendedItem> recommendedItemList;
}
