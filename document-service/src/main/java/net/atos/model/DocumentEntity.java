package net.atos.model;

import lombok.*;
import net.atos.model.enums.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
public class DocumentEntity {

    @Id
    @Field("document_id")
    @Setter(AccessLevel.NONE)
    @NotNull
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String path;

    @NotBlank
    private String name;

    @NotBlank
    private EnumDataType type;

    @NotBlank
    private String extension;

    @NotNull
    @Field("date_of_creation")
    @Builder.Default
    private LocalDateTime dateOfCreation = LocalDateTime.now();

    @NotNull
    @Field("last_accessed")
    @Builder.Default
    private LocalDateTime lastAccessed = LocalDateTime.now();

    @NotNull
    @Field("last_modified")
    @Builder.Default
    private LocalDateTime lastModified = LocalDateTime.now();

    @NotNull
    @Field("size_in_bytes")
    private Long sizeInBytes;

    @NotNull
    @Field("created_by_user_id")
    private UUID createdByUserId;

    @NotNull
    @Field("accessible_by_users")
    @Builder.Default
    private Set<UUID> accessibleByUsers = new HashSet<>();

    @NotNull
    @Field("last_modified_by_user_id")
    private UUID lastModifiedByUserId;

    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @Builder.Default
    private boolean isPublic = false;

    @Field("thumbnail_path")
    private String thumbnailPath;

    @Builder.Default
    private Set<EnumLanguages> languages = new HashSet<>();

    @Field("attributes")
    @Builder.Default
    private Map<String, String> attributes = new HashMap<>();

    @Builder
    public DocumentEntity(String path, String name, EnumDataType type, String extension, Long sizeInBytes, UUID createdByUserId) {
        this.path = path;
        this.name = name;
        this.type = type;
        this.extension = extension;
        this.sizeInBytes = sizeInBytes;
        this.createdByUserId = createdByUserId;
        this.lastModifiedByUserId = createdByUserId;
        this.accessibleByUsers.add(createdByUserId);
        initializeAttributes(type);
    }

    private void initializeAttributes(EnumDataType type) {
        attributes.clear();
        switch (type) {
            case VIDEO:
                for (VideoAttribute attr : VideoAttribute.values())
                    attributes.put(attr.name(), "");
                break;
            case AUDIO:
                for (AudioAttribute attr : AudioAttribute.values())
                    attributes.put(attr.name(), "");
                break;
            case TEXT:
                for (TextAttribute attr : TextAttribute.values())
                    attributes.put(attr.name(), "");
                break;
            case IMAGE:
                for (ImageAttribute attr : ImageAttribute.values())
                    attributes.put(attr.name(), "");
                break;
            default:
                attributes = new HashMap<>();
        }
    }

    public DocumentEntity addAttribute(String key, String value) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Attribute key cannot be null or empty");

        if (value == null)
            throw new IllegalArgumentException("Attribute value cannot be null");

        if (attributes.containsKey(key))
            throw new IllegalArgumentException("Attribute already exists: " + key);

        attributes.put(key, value);
        return this;
    }

    public DocumentEntity removeAttribute(String key) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Attribute key cannot be null or empty");

        if (!attributes.containsKey(key))
            throw new IllegalArgumentException("Attribute does not exist: " + key);

        attributes.remove(key);
        return this;
    }

    public boolean hasAttribute(String key) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Attribute key cannot be null or empty");

        return attributes.containsKey(key);
    }

    public String getAttribute(String key) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Attribute key cannot be null or empty");

        return attributes.get(key);
    }

    public DocumentEntity updateAttribute(String key, String value) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Attribute key cannot be null or empty");

        if (value == null)
            throw new IllegalArgumentException("Attribute value cannot be null");

        if (!attributes.containsKey(key))
            throw new IllegalArgumentException("Attribute does not exist: " + key);

        attributes.put(key, value);
        return this;
    }
}