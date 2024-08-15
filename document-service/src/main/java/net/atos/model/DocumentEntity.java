package net.atos.model;

import lombok.*;
import net.atos.model.enums.EnumDataType;
import net.atos.model.enums.EnumLanguages;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentEntity {
    public DocumentEntity(String path,
                          String name,
                          EnumDataType type,
                          String extension,
                          BigInteger sizeInBytes,
                          UUID createdByUserId) {
        this.path = path;
        this.name = name;
        this.type = type;
        this.extension = extension;
        this.sizeInBytes = sizeInBytes;
        this.createdByUserId = createdByUserId;
        this.lastModifiedByUserId = createdByUserId;
        this.accessibleByUsers.add(createdByUserId);
    }

    @Id
    @Field("document_id")
    @Setter(AccessLevel.NONE)
    @NotNull
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String path;

    @NotBlank
    private String name;

    @NotBlank
    private EnumDataType type = EnumDataType.UNKNOWN;

    @NotBlank
    private String extension;

    @NotNull
    @Field("date_of_creation")
    private LocalDateTime dateOfCreation = LocalDateTime.now();

    @NotNull
    @Field("last_accessed")
    private LocalDateTime lastAccessed = LocalDateTime.now();

    @NotNull
    @Field("last_modified")
    private LocalDateTime lastModified = LocalDateTime.now();

    @NotNull
    @Field("size_in_bytes")
    private BigInteger sizeInBytes;

    @NotNull
    @Field("created_by_user_id")
    private UUID createdByUserId;

    @NotNull
    @Field("accessible_by_users")
    private Set<UUID> accessibleByUsers = new HashSet<>();

    @NotNull
    @Field("last_modified_by_user_id")
    private UUID lastModifiedByUserId;

    private Set<String> tags;

    private boolean isPublic = false;

    @Field("thumbnail_path")
    private String thumbnailPath;

    private Set<EnumLanguages> languages;

    private UUID generateId() {
        return UUID.randomUUID();
    }

}

