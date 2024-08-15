package net.atos.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface TextBasedDocumentRepository extends MongoRepository<TextBasedDocument, UUID> {

}