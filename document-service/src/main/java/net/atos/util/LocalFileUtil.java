package net.atos.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class LocalFileUtil {

    public static Path concatPathToUserIdFolder(UUID userId, String fileName) {
        return Paths.get(userId.toString(), fileName);
    }

    public static String extractFileName(String fullFilePath) {
        Path path = Paths.get(fullFilePath);
        return path.getFileName().toString();
    }

    public static String extractDirectory(String fullFilePath) {
        Path path = Paths.get(fullFilePath);
        Path parent = path.getParent();
        return parent != null ? parent.toString() : "";
    }

    public static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
