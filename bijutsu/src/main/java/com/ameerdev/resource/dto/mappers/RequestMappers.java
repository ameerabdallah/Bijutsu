package com.ameerdev.resource.dto.mappers;

import com.ameerdev.models.Library;
import com.ameerdev.resource.dto.request.CreateLibraryDTO;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RequestMappers {
    @Contract("_ -> new")
    public static @NotNull Library toLibrary(@NotNull CreateLibraryDTO dto) {
        return new Library(
                -1L,
                dto.name(),
                dto.description(),
                dto.paths(),
                dto.libraryType(),
                dto.readDirection(),
                dto.seriesType()
        );
    }
}
