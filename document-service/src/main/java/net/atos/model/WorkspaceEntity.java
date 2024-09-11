package net.atos.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "workspaces")
public class WorkspaceEntity {

    public WorkspaceEntity(String name, String description, String createdByUser) {
        this.name = name;
        this.description = description;
        this.createdByUser = createdByUser;
        accessibleByUsers = new HashSet<>();
        accessibleByUsers.add(createdByUser);
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
    private String createdByUser;

    @DBRef
    @JsonManagedReference
    private Set<DocumentEntity> documents = new HashSet<>();

    @NotNull
    @Field("accessible_by_users")
    @Setter(AccessLevel.NONE)
    private Set<String> accessibleByUsers = new HashSet<>();

    private boolean isDeleted = false;

    public void addUser(String email) {
        if (email == null)
            throw new IllegalArgumentException("User email cannot be null");
        if (accessibleByUsers.contains(email))
            throw new IllegalArgumentException("User already has access: " + email);
        accessibleByUsers.add(email);
    }

    public WorkspaceEntity removeUser(String email) {
        if (email == null)
            throw new IllegalArgumentException("User email cannot be null");

        if (!accessibleByUsers.contains(email))
            throw new IllegalArgumentException("User does not have access: " + email);

        if (email.equals(createdByUser))
            throw new IllegalArgumentException("cannot remove the owner: " + email);

        accessibleByUsers.remove(email);
        return this;
    }

    public boolean isUserAuthorized(String email) {
        if (email == null)
            throw new IllegalArgumentException("User email cannot be null");
        return createdByUser.equals(email) || accessibleByUsers.contains(email);
    }

    public Set<String> getAccessibleUsers() {
        return new HashSet<>(accessibleByUsers);
    }

    public void addDocument(DocumentEntity document) {
        if (document == null)
            throw new IllegalArgumentException("Document cannot be null");

        if (documents.contains(document))
            throw new IllegalArgumentException("Document already exists in this workspace: " + document.getId());

        documents.add(document);
        document.addWorkspace(this);
    }

    public void removeDocument(DocumentEntity document) {
        if (document == null)
            throw new IllegalArgumentException("Document cannot be null");

        if (!documents.contains(document))
            throw new IllegalArgumentException("Document does not exist in this workspace: " + document.getId());

        documents.remove(document);
        document.removeWorkspace(this);
    }

    @Override
    public String toString() {
        return "WorkspaceEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateOfCreation=" + dateOfCreation +
                ", createdByUser=" + createdByUser +
                ", workspaces=" + documents.stream().map(DocumentEntity::getId).collect(Collectors.toSet()) +
                ", accessibleByUsers=" + accessibleByUsers +
                ", isDeleted=" + isDeleted +
                '}';
    }
}