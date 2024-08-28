package net.atos.dto.workspace;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class WorkspaceUserDto {

    @NotNull(message = "workspaceId cannot be null")
    private UUID workspaceId;

    @NotBlank(message = "userId cannot be null")
    private UUID userId;
}
