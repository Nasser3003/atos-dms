package net.atos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class AbstractDocumentEntity {

    @Id
    @Field("document_id")
    @Setter(AccessLevel.NONE)
    @NotNull
    private UUID id;

    @NotBlank
    private String path;

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotBlank
    private String extension;

    @NotNull
    @Field("date_of_creation")
    private LocalDateTime dateOfCreation;

    @NotNull
    @Field("last_accessed")
    private LocalDateTime lastAccessed;

    @NotNull
    @Field("last_modified")
    private LocalDateTime lastModified;

    @NotNull
    @Field("size_in_bytes")
    private Long sizeInBytes;

    @NotNull
    @Field("created_by_user_id")
    private UUID createdByUserId;

    @NotNull
    @Field("accessable_by_users")
    private Set<UUID> accessableByUsers;

    @NotNull
    @Field("last_modified_by_user_id")
    private UUID lastModifiedByUserId;

    private String summary;

    private Set<String> tags;

    private boolean isPublic = false;

    @Field("thumbnail_path")
    private String thumbnailPath;

    private Set<EnumLanguages> languages;

}
