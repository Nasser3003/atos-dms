package net.atos.dto.workspace;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class WorkspaceSimpleDto {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime dateOfCreation;
    private UUID createdByUserId;
    private Set<UUID> accessibleByUsers;
}