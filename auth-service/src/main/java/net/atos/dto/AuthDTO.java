package net.atos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthDTO {
    private String email;
    private String password;
}
