package net.atos.repository;

import net.atos.model.DocumentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface DocumentRepository extends MongoRepository<DocumentEntity, UUID> {

}