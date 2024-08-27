package net.atos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "workspaces")
public class WorkspaceEntity {

    public WorkspaceEntity(String name, String description, UUID createdByUserId) {
        this.name = name;
        this.description = description;
        this.createdByUserId = createdByUserId;
    }

    @Id
    @Field("workspace_id")
    @Setter(AccessLevel.NONE)
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Field("date_of_creation")
    @Setter(AccessLevel.NONE)
    private LocalDateTime dateOfCreation = LocalDateTime.now();

    @NotNull
    @Field("created_by_user_id")
    @Setter(AccessLevel.NONE)
    private UUID createdByUserId;

    @DBRef
    @ToString.Exclude
    private Set<DocumentEntity> documents = new HashSet<>();

    @NotNull
    @Field("accessible_by_users")
    @Setter(AccessLevel.NONE)
    private Set<UUID> accessibleByUsers = new HashSet<>();

    public void addUser(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("User id cannot be null");
        if (accessibleByUsers.contains(id))
            throw new IllegalArgumentException("User already has access: " + id);
        accessibleByUsers.add(id);
    }

    public WorkspaceEntity removeUser(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("User id cannot be null");
        if (!accessibleByUsers.contains(id))
            throw new IllegalArgumentException("User does not have access: " + id);
        accessibleByUsers.remove(id);
        return this;
    }

    public boolean hasUserAccess(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("User id cannot be null");
        return accessibleByUsers.contains(id);
    }

    public Set<UUID> getAccessibleUsers() {
        return new HashSet<>(accessibleByUsers);
    }

}