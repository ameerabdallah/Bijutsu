package com.ameerdev.repositories;

import com.ameerdev.jooq.generated.tables.records.SeriesRecord;
import com.ameerdev.models.Series;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

import static com.ameerdev.jooq.generated.tables.Series.SERIES;

@ApplicationScoped
public class SeriesRepositoryImpl implements SeriesRepository {
    @Inject
    DSLContext dsl;

    @Override
    public List<Series> findAll() {
        return dsl.selectFrom(SERIES)
                .fetchInto(Series.class);
    }

    @Override
    public List<Series> findSeriesByLibraryId(long libraryId) {
        return dsl.selectFrom(SERIES)
                .where(SERIES.LIBRARY_ID.eq(libraryId))
                .fetchInto(Series.class);
    }

    @Override
    public Optional<Series> fetchSeriesById(Long id) {
        return dsl.selectFrom(SERIES)
                .where(SERIES.ID.eq(id))
                .fetchOptionalInto(Series.class);
    }

    @Override
    public Optional<Series> fetchSeriesByMetadataSourceId(String metadataSourceId) {
        return dsl.selectFrom(SERIES)
                .where(SERIES.METADATA_SOURCE_ID.eq(metadataSourceId))
                .fetchOptionalInto(Series.class);
    }

    @Override
    public Optional<Series> createSeries(Series series) {
        SeriesRecord record = dsl.newRecord(SERIES);
        fillFromDomain(series, record);

        SeriesRecord result = dsl.insertInto(SERIES)
                .set(record)
                .returning()
                .fetchOne();

        return Optional.ofNullable(result).map(r -> r.into(Series.class));
    }

    @Override
    public Optional<Series> updateSeries(Series series) {
        SeriesRecord record = dsl.newRecord(SERIES);
        record.setId(series.getId());
        fillFromDomain(series, record);

        int rowsUpdated = dsl.update(SERIES)
                .set(record)
                .where(SERIES.ID.eq(series.getId()))
                .execute();

        return rowsUpdated > 0 ? Optional.of(series) : Optional.empty();
    }

    private void fillFromDomain(Series series, SeriesRecord record) {
        record.setLibraryId(series.getLibraryId());
        record.setPath(series.getPath());
        record.setTitle(series.getTitle());
        record.setAuthor(series.getAuthor());
        record.setDescription(series.getDescription());
        record.setReleaseYear(series.getReleaseYear());
        record.setMetadataSourceId(series.getMetadataSourceId());
    }

    @Override
    public boolean deleteById(long id) {
        int rowsDeleted = dsl.deleteFrom(SERIES)
                .where(SERIES.ID.eq(id))
                .execute();

        return rowsDeleted > 0;
    }
}
