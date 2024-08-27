package net.atos.dto.workspace;

import lombok.*;
import net.atos.model.DocumentEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class WorkspaceReadDto {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime dateOfCreation;
    private UUID createdByUserId;
    private Set<DocumentEntity> documents;
    private Set<UUID> accessibleByUsers;

}
