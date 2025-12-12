package com.ameerdev.repositories.jooq.mapper;

import com.ameerdev.models.Series;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static com.ameerdev.jooq.generated.Tables.SERIES;

public class SeriesMapper implements RecordMapper<Record, Series> {
    @Override
    public @Nullable Series map(Record record) {
        return new Series(
                record.get(SERIES.ID, Long.class),
                record.get(SERIES.LIBRARY_ID, Long.class),
                record.get(SERIES.PATH, String.class),
                record.get(SERIES.TITLE, String.class),
                record.get(SERIES.AUTHOR, String.class),
                record.get(SERIES.DESCRIPTION, String.class),
                record.get(SERIES.RELEASE_YEAR, Integer.class),
                record.get(SERIES.METADATA_SOURCE_ID, String.class)
        );
    }
}
