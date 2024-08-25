package net.atos.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePath {

    public static String extractFileName(String fullFilePath) {
        Path path = Paths.get(fullFilePath);
        return path.getFileName().toString();
    }
    public static String extractDirectory(String fullFilePath) {
        Path path = Paths.get(fullFilePath);
        Path parent = path.getParent();
        return parent != null ? parent.toString() : "";
    }
}
