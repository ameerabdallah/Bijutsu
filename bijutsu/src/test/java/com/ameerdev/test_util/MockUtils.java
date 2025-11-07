package com.ameerdev.test_util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MockUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.findAndRegisterModules();
    }

    public static <T> T loadMockResponseObject(String resourcePath, Class<T> clazz) throws IOException {
        String json = loadMockResponse(resourcePath);
        return MAPPER.readValue(json, clazz);
    }

    public static String loadMockResponse(String resourcePath) throws IOException {
        try (InputStream is = MockUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
