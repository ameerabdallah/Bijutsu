package com.ameerdev.repositories.jooq.mapper;

import com.ameerdev.jooq.generated.enums.BookType;
import com.ameerdev.jooq.generated.enums.ReadDirection;
import com.ameerdev.jooq.generated.enums.SeriesType;
import com.ameerdev.models.Library;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.jooq.RecordMapper;

import java.util.ArrayList;
import java.util.List;

import static com.ameerdev.jooq.generated.Tables.LIBRARY;
import static com.ameerdev.repositories.jooq.mapper.EnumConverters.BookTypeConverter.toDomainBookType;
import static com.ameerdev.repositories.jooq.mapper.EnumConverters.ReadDirectionConverter.toDomainReadDirection;
import static com.ameerdev.repositories.jooq.mapper.EnumConverters.SeriesTypeConverter.toDomainSeriesType;

public class LibraryMapper implements RecordMapper<Record, Library> {

    private final List<String> paths;

    public LibraryMapper(List<String> paths) {
        this.paths = paths;
    }

    @Override
    public @Nullable Library map(Record record) {
        return new Library(
                record.get(LIBRARY.ID, Long.class),
                record.get(LIBRARY.NAME, String.class),
                record.get(LIBRARY.DESCRIPTION, String.class),
                new ArrayList<>(paths),
                toDomainBookType(record.get(LIBRARY.BOOK_TYPE, BookType.class)),
                toDomainReadDirection(record.get(LIBRARY.READ_DIRECTION, ReadDirection.class)),
                toDomainSeriesType(record.get(LIBRARY.SERIES_TYPE, SeriesType.class))
        );
    }
}
