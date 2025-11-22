package com.ameerdev.repositories;

import com.ameerdev.jooq.generated.tables.records.ReleaseRecord;
import com.ameerdev.models.Release;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

import static com.ameerdev.jooq.generated.tables.Release.RELEASE;
import static com.ameerdev.mapper.EnumConverters.ReleaseTypeConverter.toJooqReleaseType;

@ApplicationScoped
public class ReleaseRepositoryImpl implements ReleaseRepository {
    @Inject
    DSLContext dsl;

    @Override
    public List<Release> findAll() {
        return dsl.selectFrom(RELEASE)
                .fetchInto(Release.class);
    }

    @Override
    public Optional<Release> findById(long id) {
        return dsl.selectFrom(RELEASE)
                .where(RELEASE.ID.eq(id))
                .fetchOptionalInto(Release.class);
    }

    @Override
    public List<Release> findBySeriesId(long seriesId) {
        return dsl.selectFrom(RELEASE)
                .where(RELEASE.SERIES_ID.eq(seriesId))
                .orderBy(RELEASE.INDEX.asc())
                .fetchInto(Release.class);
    }

    @Override
    public Optional<Release> findByMetadataSourceId(String metadataSourceId) {
        return dsl.selectFrom(RELEASE)
                .where(RELEASE.METADATA_SOURCE_ID.eq(metadataSourceId))
                .fetchOptionalInto(Release.class);
    }

    @Override
    public Optional<Release> create(Release release) {
        ReleaseRecord record = dsl.newRecord(RELEASE);
        fillRecordFromDomain(release, record);

        ReleaseRecord result = dsl.insertInto(RELEASE)
                .set(record)
                .returning()
                .fetchOne();

        return Optional.ofNullable(result).map(r -> r.into(Release.class));
    }

    @Override
    public Optional<Release> update(Release release) {
        ReleaseRecord record = dsl.newRecord(RELEASE);
        record.setId(release.getId());
        fillRecordFromDomain(release, record);

        int rowsUpdated = dsl.update(RELEASE)
                .set(record)
                .where(RELEASE.ID.eq(release.getId()))
                .execute();

        return rowsUpdated > 0 ? Optional.of(release) : Optional.empty();
    }

    private void fillRecordFromDomain(Release release, ReleaseRecord record) {
        record.setSeriesId(release.getSeriesId());
        record.setIndex(release.getIndex());
        record.setTitle(release.getTitle());
        record.setFilePath(release.getFilePath());
        record.setReleaseDate(release.getReleaseDate());
        record.setReleaseType(toJooqReleaseType(release.getReleaseType()));
        record.setMetadataSourceId(release.getMetadataSourceId());
    }

    @Override
    public boolean deleteById(long id) {
        int rowsDeleted = dsl.deleteFrom(RELEASE)
                .where(RELEASE.ID.eq(id))
                .execute();

        return rowsDeleted > 0;
    }
}
