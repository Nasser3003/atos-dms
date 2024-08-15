package net.atos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDTO {
    private UUID uuid;
    private String email;
    private String token;
}
