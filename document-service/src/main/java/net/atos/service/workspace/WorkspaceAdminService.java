package net.atos.service.workspace;

import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.exception.NotFoundException;
import net.atos.mapper.WorkspaceMapper;
import net.atos.model.DocumentEntity;
import net.atos.model.WorkspaceEntity;
import net.atos.repository.DocumentRepository;
import net.atos.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkspaceAdminService extends AbstractWorkspaceService {

    @Autowired
    public WorkspaceAdminService(WorkspaceRepository repository, DocumentRepository documentRepository) {
        super(repository, documentRepository);
    }

    @Override
    @Transactional
    public WorkspaceReadDto addDocument(UUID documentId, UUID workspaceId) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceId);
        DocumentEntity documentEntity = documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document with ID " + documentId + " not found"));

        workspaceEntity.addDocument(documentEntity);
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    @Transactional
    public WorkspaceReadDto removeDocument(UUID documentId, UUID workspaceId) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceId);
        DocumentEntity documentEntity = documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document with ID " + documentId + " not found"));

        workspaceEntity.removeDocument(documentEntity);
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspaces() {
        return repository.findAll().stream()
                .map(WorkspaceMapper::mapToReadWorkspace)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceReadDto getWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    @Transactional
    public WorkspaceReadDto updateWorkspace(WorkspaceEditDto workspaceEditDto) {
        return WorkspaceMapper.mapToReadWorkspace(updateWorkspaceHelper(workspaceEditDto));
    }

    @Override
    public void deleteWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        workspaceEntity.setDeleted(true);
        repository.save(workspaceEntity);
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {
        return null;
    }
}
