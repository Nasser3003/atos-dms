package net.atos.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "userId")
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long userId;


    public UserInformation(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String email;
    private String password;
    private String description;

}
