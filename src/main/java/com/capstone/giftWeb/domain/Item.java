package com.capstone.giftWeb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {
    @Id
    @Column(name = "item_id", nullable = false)
    @JsonIgnore
    private Long id;

    private String title;

    private String company;

    private Integer price;

    private Integer category;

    private String image;

    private String href;

}
