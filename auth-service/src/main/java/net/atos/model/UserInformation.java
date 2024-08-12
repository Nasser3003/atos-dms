package net.atos.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserInformation {

    @Id
    @Setter(AccessLevel.NONE)
    private long userId;

    private String username;
    private String password;
    private String description;


}
