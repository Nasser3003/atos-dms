package net.atos.repository;

import net.atos.model.DocumentEntity;
import net.atos.model.enums.EnumDataType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends MongoRepository<DocumentEntity, UUID> {
    List<DocumentEntity> findAllByType(EnumDataType type);
    List<DocumentEntity> findAllByTagsContainingIgnoreCase(String tag);
    List<DocumentEntity> findAllByFilePathContaining(String name);
}