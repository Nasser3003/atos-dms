package net.atos.util;

import net.atos.dto.DocumentDto;

public class DocumentValidator {
    public static void validateDocument(DocumentDto documentDto) {
        if (documentDto == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }

        if (isNullOrEmpty(documentDto.getPath()) ||
                isNullOrEmpty(documentDto.getName()) ||
                documentDto.getType() == null ||
                isNullOrEmpty(documentDto.getExtension())) {
            throw new IllegalArgumentException("Document fields cannot be null or empty");
        }
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
