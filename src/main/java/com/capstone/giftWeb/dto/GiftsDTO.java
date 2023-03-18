package com.capstone.giftWeb.dto;

import com.capstone.giftWeb.domain.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class GiftsDTO {

    private List<Item> gifts;
}
