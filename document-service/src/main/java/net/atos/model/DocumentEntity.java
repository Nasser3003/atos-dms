package net.atos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import net.atos.exception.AttributeException;
import net.atos.model.enums.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@CompoundIndex(def = "{'filePath': 'text', 'tags': 'text'}")
@Document(collection = "documents")
public class DocumentEntity {

    public DocumentEntity(String filePath, EnumDataType type, Long sizeInBytes, UUID createdByUserId) {
        this.filePath = filePath;
        this.type = type;
        this.sizeInBytes = sizeInBytes;
        this.createdByUserId = createdByUserId;
        this.lastModifiedByUserId = createdByUserId;
        initializeAttributes(type);
        this.accessibleByUsers = new HashSet<>();
        accessibleByUsers.add(createdByUserId);
    }

    @Id
    @Field("document_id")
    @Setter(AccessLevel.NONE)
    private UUID id = UUID.randomUUID();

    @NotBlank
    @Field("storage_url")
    @Value("${file.storage.location}")
    private String storageUrl;

    @NotBlank
    @Indexed
    private String filePath;

    @NotBlank
    @Indexed
    private EnumDataType type;

    @Field("date_of_creation")
    @Setter(AccessLevel.NONE)
    private LocalDateTime dateOfCreation = LocalDateTime.now();

    @NotNull
    @Field("last_accessed")
    private LocalDateTime lastAccessed = LocalDateTime.now();

    @NotNull
    @Field("last_accessed_by_user_id")
    private UUID lastAccessedByUserId;

    @NotNull
    @Field("last_modified")
    private LocalDateTime lastModified = LocalDateTime.now();

    @NotNull
    @Setter(AccessLevel.NONE)
    @Field("size_in_bytes")
    @Min(1)
    private Long sizeInBytes;

    @NotNull
    @Field("created_by_user_id")
    @Setter(AccessLevel.NONE)
    private UUID createdByUserId;

    @NotNull
    @Field("accessible_by_users")
    @Setter(AccessLevel.NONE)
    private Set<UUID> accessibleByUsers;

    @NotNull
    @Field("last_modified_by_user_id")
    private UUID lastModifiedByUserId;

    private Set<String> tags = new HashSet<>();

    private boolean isPublic = false;

    private boolean isDeleted = false;

    @Field("thumbnail_path")
    private String thumbnailPath;

    @DBRef
    @Setter(AccessLevel.NONE)
    @JsonBackReference
    private Set<WorkspaceEntity> workspaces = new HashSet<>();

    private Set<EnumLanguages> languages = new HashSet<>();

    @Field("attributes")
    @Setter(AccessLevel.NONE)
    private Map<String, String> attributes = new HashMap<>();

    public boolean isUserAuthorized(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("User id cannot be null");
        return createdByUserId.equals(id) || accessibleByUsers.contains(id);
    }

    public Set<WorkspaceEntity> getWorkspaces() {
        return new HashSet<>(workspaces);
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes);
    }

    public void addAttribute(String key, String value) {
        if (key == null || key.isEmpty())
            throw new AttributeException("Attribute key cannot be null or empty");

        if (value == null)
            throw new AttributeException("Attribute value cannot be null");

        if (attributes.containsKey(key))
            throw new AttributeException("Attribute already exists: " + key);

        attributes.put(key, value);
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

    public void setType(EnumDataType type) {
        this.type = type;
        initializeAttributes(type);
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

    public void addUser(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("User id cannot be null or empty");

        if (accessibleByUsers.contains(id))
            throw new IllegalArgumentException("User already has access: " + id);

        accessibleByUsers.add(id);
    }

    public DocumentEntity removeUser(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("User id cannot be null or empty");

        if (!accessibleByUsers.contains(id))
            throw new IllegalArgumentException("User does not have access: " + id);

        if (id.equals(createdByUserId))
            throw new IllegalArgumentException("cannot remove the owner: " + id);

        accessibleByUsers.remove(id);
        return this;
    }

    public Set<UUID> getAccessibleByUsers() {
        return new HashSet<>(accessibleByUsers);
    }

    public void addTag(String tag) {
        tags.add(tag.toLowerCase());
    }

    public void removeTag(String tag) {
        tags.remove(tag.toLowerCase());
    }

    public void addWorkspace(WorkspaceEntity workspace) {
        if (workspace == null)
            throw new IllegalArgumentException("Workspace cannot be null");
        if (workspaces.contains(workspace))
            throw new IllegalArgumentException("Document already exists in this workspace: " + workspace.getId());
        workspaces.add(workspace);
    }

    public DocumentEntity removeWorkspace(WorkspaceEntity workspace) {
        if (workspace == null)
            throw new IllegalArgumentException("Workspace cannot be null");
        if (!workspaces.contains(workspace))
            throw new IllegalArgumentException("Document is not in this workspace: " + workspace.getId());
        workspaces.remove(workspace);
        return this;
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

    @Override
    public String toString() {
        return "DocumentEntity{" +
                "id=" + id +
                ", storageUrl='" + storageUrl + '\'' +
                ", filePath='" + filePath + '\'' +
                ", type=" + type +
                ", dateOfCreation=" + dateOfCreation +
                ", lastAccessed=" + lastAccessed +
                ", lastAccessedByUserId=" + lastAccessedByUserId +
                ", lastModified=" + lastModified +
                ", sizeInBytes=" + sizeInBytes +
                ", createdByUserId=" + createdByUserId +
                ", accessibleByUsers=" + accessibleByUsers +
                ", lastModifiedByUserId=" + lastModifiedByUserId +
                ", tags=" + tags +
                ", isPublic=" + isPublic +
                ", isDeleted=" + isDeleted +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", workspaces=" + workspaces.stream().map(WorkspaceEntity::getName).collect(Collectors.toSet()) +
                ", languages=" + languages +
                ", attributes=" + attributes +
                '}';
    }
}