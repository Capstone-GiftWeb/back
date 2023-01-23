package com.capstone.giftWeb.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Getter @Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;



    @Builder
    public Member(Long id,String name, String email, String password) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public Member() {

    }
}
