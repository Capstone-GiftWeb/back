package com.capstone.giftWeb.domain;

import com.capstone.giftWeb.enums.Authority;
import com.capstone.giftWeb.enums.Gender;
//import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Member implements Serializable {
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
