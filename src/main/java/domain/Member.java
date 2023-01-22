package domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue()
    @Column(name = "member_id", nullable = false)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;



    @Builder
    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


}
