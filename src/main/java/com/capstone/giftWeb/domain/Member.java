package com.capstone.giftWeb.domain;

import com.capstone.giftWeb.enums.Authority;
import com.capstone.giftWeb.enums.Gender;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int age;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String ip;


    public boolean matchPassword(String password){
        return this.password.equals(password);
    }



    @Builder
    public Member(Long id,String name, String email, String password,Gender gender,int age,Authority authority) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender=gender;
        this.authority=authority;
        this.age=age;
    }

}
