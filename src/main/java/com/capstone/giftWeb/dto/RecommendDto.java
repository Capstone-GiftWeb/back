package com.capstone.giftWeb.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import javax.persistence.Column;
import java.util.List;

@Data
@Getter
@Setter
public class RecommendDto {
    private List<RecommendedItem> recommendedItemList;
}
