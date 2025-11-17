package com.ameerdev.mapper;

import com.ameerdev.jooq.generated.enums.ReleaseType;
import com.ameerdev.models.Release;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.jooq.RecordMapper;

import java.time.LocalDate;

import static com.ameerdev.jooq.generated.Tables.RELEASE;
import static com.ameerdev.mapper.EnumConverters.ReleaseTypeConverter.toDomainReleaseType;

public class ReleaseMapper implements RecordMapper<Record, Release> {
    @Override
    public @Nullable Release map(Record record) {
        return new Release(
                record.get(RELEASE.ID, Long.class),
                record.get(RELEASE.INDEX, Integer.class),
                record.get(RELEASE.TITLE, String.class),
                record.get(RELEASE.FILE_PATH, String.class),
                record.get(RELEASE.RELEASE_DATE, LocalDate.class),
                toDomainReleaseType(record.get(RELEASE.RELEASE_TYPE, ReleaseType.class)),
                record.get(RELEASE.METADATA_SOURCE_ID, String.class)
        );
    }
}
