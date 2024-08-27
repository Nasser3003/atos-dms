package net.atos.repository;

import net.atos.model.WorkspaceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface WorkspaceRepository extends MongoRepository<WorkspaceEntity, UUID> {
}
