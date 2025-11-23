package com.ameerdev.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * A utility class for handling file operations.
 * All methods should be mocked during unit testing to avoid direct file system access.
 */
public class FileHandler {
    private FileHandler() {
        // Private constructor to prevent instantiation
    }

    public static Stream<Path> list(Path directory) throws IOException {
        return Files.list(directory);
    }

    public static boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    public static boolean isRegularFile(Path path) {
        return Files.isRegularFile(path);
    }

    public static Stream<Path> walk(Path path) throws IOException {
        return Files.walk(path);
    }

    public static boolean isHidden(Path path) {
        try {
            return Files.isHidden(path);
        } catch (IOException e) {
            return false; // assume not hidden
        }
    }

    public static boolean isReadable(Path path) {
        return Files.isReadable(path);
    }
}
