package net.atos.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class LoginResponseDTO {
    private UUID uuid;
    private String email;
    private String token;
}
