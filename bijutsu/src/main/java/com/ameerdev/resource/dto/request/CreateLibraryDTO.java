package com.ameerdev.resource.dto.request;

import com.ameerdev.models.enums.BookType;
import com.ameerdev.models.enums.ReadDirection;
import com.ameerdev.models.enums.SeriesType;

import java.util.List;

public record CreateLibraryDTO (
        String name,
        String description,
        ReadDirection readDirection,
        BookType libraryType,
        SeriesType seriesType,
        List<String> paths
){
}
