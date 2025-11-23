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
}
