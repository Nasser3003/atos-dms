package net.atos.dto.workspace;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class WorkspaceEditDto {

    @NotNull(message = "ID cannot be null")
    private UUID id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;
}}
