package net.atos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.atos.model.enums.EnumDataType;
import net.atos.model.enums.EnumLanguages;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentReadOnlyDto {
    private UUID id;
    private String pathToTheDirectory;
    private String name;
    private EnumDataType type;
    private Long sizeInBytes;
    private String extension;
    private Set<UUID> accessibleByUsers;
    private Set<String> tags;
    private boolean isPublic;
    private String thumbnailPath;
    private Set<EnumLanguages> languages;
    private Map<String, String> attributes;

    private LocalDateTime dateOfCreation;
    private LocalDateTime lastAccessed;
    private LocalDateTime lastModified;
    private UUID createdByUserId;
    private UUID lastModifiedByUserId;
    private UUID lastAccessedByUserId;
}