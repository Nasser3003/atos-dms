package net.atos.service.search;

import net.atos.model.DocumentEntity;
import net.atos.model.enums.EnumDataType;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AdminSearchService extends AbstractSearchService {

    @Autowired
    public AdminSearchService(DocumentRepository documentRepository) {
        super(documentRepository);
    }

    @Override
    public Set<DocumentEntity> search(String query) {
        return searchHelper(query);
    }

}