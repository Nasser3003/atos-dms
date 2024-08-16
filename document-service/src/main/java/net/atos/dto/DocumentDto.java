package net.atos.dto;

import lombok.Builder;
import lombok.Data;
import net.atos.model.enums.EnumDataType;
import net.atos.model.enums.EnumLanguages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class DocumentDto {
    @NotNull
    private UUID id;

    @NotBlank
    private String path;

    @NotBlank
    private String name;

    @NotNull
    private EnumDataType type;

    @NotBlank
    private String extension;

    @NotNull
    private LocalDateTime dateOfCreation;

    @NotNull
    private LocalDateTime lastAccessed;

    @NotNull
    private LocalDateTime lastModified;

    @NotNull
    private Long sizeInBytes;

    @NotNull
    private UUID createdByUserId;

    @NotNull
    private Set<UUID> accessibleByUsers;

    @NotNull
    private UUID lastModifiedByUserId;

    private Set<String> tags;

    private boolean isPublic;

    private String thumbnailPath;

    private Set<EnumLanguages> languages;

    private Map<String, String> attributes;

}