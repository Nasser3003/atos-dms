package net.atos.dto.document;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class DocumentUserDto {

    @NotNull(message = "documentId cannot be null")
    private UUID documentId;

    @NotBlank(message = "userId cannot be null")
    private UUID userId;
}
