package net.atos.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TextDocumentDto extends AbstractDocumentDto {

    private Long wordCount;
}
