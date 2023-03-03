package com.capstone.giftWeb.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
public class Item {
    @Id
    @Column(name = "item_id", nullable = false)
    private Long id;

    private Integer category;

}
