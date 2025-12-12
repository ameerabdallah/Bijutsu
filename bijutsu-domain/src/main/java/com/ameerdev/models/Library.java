package com.ameerdev.models;

import com.ameerdev.models.enums.BookType;
import com.ameerdev.models.enums.ReadDirection;
import com.ameerdev.models.enums.SeriesType;
import lombok.*;

import java.util.List;

/**
 * Represents a library containing books.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Library {
    private long id;
    private String name;
    private String description;
    private List<String> paths;
    private BookType bookType;
    private ReadDirection readDirection;
    private SeriesType seriesType;
}