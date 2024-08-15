package net.atos.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.atos.model.enums.EnumLanguages;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
public class AbstractDocumentDto {
    private final UUID id;
    private final String path;
    private final String name;
    private final String type;
    private final String extension;
    private final LocalDateTime dateOfCreation;
    private final LocalDateTime lastAccessed;
    private final LocalDateTime lastModified;
    private final Long sizeInBytes;
    private final UUID createdByUserId;
    private final Set<UUID> accessibleByUsers;
    private final UUID lastModifiedByUserId;
    private final String summary;
    private final Set<String> tags;
    private final boolean isPublic;
    private final String thumbnailPath;
    private final Set<EnumLanguages> languages;
}