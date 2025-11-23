package com.ameerdev.repositories;

import com.ameerdev.jooq.generated.tables.records.SeriesRecord;
import com.ameerdev.models.Series;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.DSLContext;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public Optional<Series> findSeriesByMetadataSourceId(String metadataSourceId) {
        return Optional.empty();
    }


    @Override
    public Set<String> findSeriesPathsByLibraryId(long libraryId) {
        return Set.of();
    }

    @Override
    public void deleteBySeriesPaths(Collection<String> seriesPath) {

    }

    @Override
    public void deleteByIds(Collection<Long> seriesIds) {
        dsl.deleteFrom(SERIES)
                .where(SERIES.ID.in(seriesIds))
                .execute();
    }

    @Override
    public Optional<Series> create(Series series) {
        SeriesRecord record = dsl.newRecord(SERIES);
        fillFromDomain(series, record);

        return dsl.insertInto(SERIES)
                .set(record)
                .returning()
                .fetchOptionalInto(Series.class);
    }

    @Override
    public Optional<Series> update(Series series) {
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
        if (series.getPath() != null) {
            record.setPath(series.getPath());
        }
        if (series.getTitle() != null) {
            record.setTitle(series.getTitle());
        }
        if (series.getAuthor() != null) {
            record.setAuthor(series.getAuthor());
        }
        if (series.getDescription() != null) {
            record.setDescription(series.getDescription());
        }
        if (series.getReleaseYear() != null) {
            record.setReleaseYear(series.getReleaseYear());
        }
        if (series.getMetadataSourceId().isPresent()) {
            record.setMetadataSourceId(series.getMetadataSourceId().get());
        }
    }

    @Override
    public boolean deleteById(long id) {
        int rowsDeleted = dsl.deleteFrom(SERIES)
                .where(SERIES.ID.eq(id))
                .execute();

        return rowsDeleted > 0;
    }
}
