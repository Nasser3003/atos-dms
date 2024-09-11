package net.atos.dto.document;

import lombok.*;
import net.atos.dto.workspace.WorkspaceSimpleDto;
import net.atos.model.enums.EnumDataType;
import net.atos.model.enums.EnumLanguages;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class DocumentReadOnlyDto {

    private UUID id;
    private String filePath;
    private EnumDataType type;
    private Long sizeInBytes;
    private Set<String> accessibleByUsers;
    private Set<String> tags;
    private boolean isPublic;
    private String thumbnailPath;
    private Set<EnumLanguages> languages;
    private Map<String, String> attributes;

    private LocalDateTime dateOfCreation;
    private LocalDateTime lastAccessed;
    private LocalDateTime lastModified;
    private String createdByUser;
    private String lastModifiedByUser;
    private String lastAccessedByUser;
    private Set<WorkspaceSimpleDto> workspaces;


}