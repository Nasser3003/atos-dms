package net.atos.service.document;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

@Getter
@RequiredArgsConstructor
public class FileDownloadInfo {
    private final Resource resource;
    private final String fileName;
    private final String contentType;
    private final long contentLength;
}