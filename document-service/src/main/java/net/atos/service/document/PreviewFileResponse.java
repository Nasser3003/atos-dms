package net.atos.service.document;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PreviewFileResponse {
    private final String filename;
    private final String content;
}