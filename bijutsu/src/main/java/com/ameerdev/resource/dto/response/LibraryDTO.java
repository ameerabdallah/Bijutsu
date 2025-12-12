package com.ameerdev.resource.dto.response;

import java.util.List;

public record LibraryDTO(
        Long id,
        String name,
        String description,
        List<String> paths
) {
}
