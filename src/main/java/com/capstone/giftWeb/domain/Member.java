package com.capstone.giftWeb.domain;

import com.capstone.giftWeb.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

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


    public boolean matchPassword(String password){
        return this.password.equals(password);
    }



    @Builder
    public Member(Long id,String name, String email, String password,Gender gender,int age) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender=gender;
        this.age=age;
    }

}
