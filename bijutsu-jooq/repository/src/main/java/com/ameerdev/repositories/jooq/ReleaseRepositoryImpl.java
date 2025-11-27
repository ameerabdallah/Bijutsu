package com.ameerdev.repositories.jooq;

import com.ameerdev.jooq.generated.tables.records.ReleaseRecord;
import com.ameerdev.models.Release;
import com.ameerdev.repositories.ReleaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.ameerdev.jooq.generated.tables.Release.RELEASE;
import static com.ameerdev.repositories.jooq.mapper.EnumConverters.ReleaseTypeConverter.toJooqReleaseType;

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
    public List<Release> createBatch(Collection<Release> releases) {
        // Build values manually, excluding ID
        Field<?>[] fieldsWithoutId = Arrays.stream(RELEASE.fields())
                .filter(f -> !f.equals(RELEASE.ID))
                .toArray(Field[]::new);

        var insert = dsl.insertInto(RELEASE, fieldsWithoutId);

        for (Release release : releases) {
            ReleaseRecord record = dsl.newRecord(RELEASE);
            fillRecordFromDomain(release, record);

            Object[] values = Arrays.stream(fieldsWithoutId)
                    .map(record::get)
                    .toArray();

            insert = insert.values(values);
        }

        return insert
                .returningResult(RELEASE.fields())
                .fetch()
                .into(Release.class);
    }

    @Override
    public void updateBatch(Collection<Release> releases) {
        List<ReleaseRecord> releaseRecords = releases.stream().map(release -> {
            ReleaseRecord record = dsl.newRecord(RELEASE);
            fillRecordFromDomainWithId(release, record);
            return record;
        }).toList();

        dsl.batchUpdate(releaseRecords).execute();
    }

    @Override
    public Optional<Release> update(Release release) {
        ReleaseRecord record = dsl.newRecord(RELEASE);
        record.setId(release.getId());
        fillRecordFromDomainWithId(release, record);

        int rowsUpdated = dsl.update(RELEASE)
                .set(record)
                .where(RELEASE.ID.eq(release.getId()))
                .execute();

        return rowsUpdated > 0 ? Optional.of(release) : Optional.empty();
    }

    @Override
    public void deleteByIds(Collection<Long> releaseIds) {
        dsl.deleteFrom(RELEASE)
                .where(RELEASE.ID.in(releaseIds))
                .execute();
    }

    private void fillRecordFromDomainWithId(Release release, ReleaseRecord record) {
        record.setId(release.getId());
        fillRecordFromDomain(release, record);
    }

    private void fillRecordFromDomain(Release release, ReleaseRecord record) {
        record.setSeriesId(release.getSeriesId());
        record.setIndex(release.getIndex());
        if (release.getTitle() != null) {
            record.setTitle(release.getTitle());
        }
        if (release.getFilePath() != null) {
            record.setFilePath(release.getFilePath());
        }
        if (release.getMetadataSourceId() != null) {
            record.setMetadataSourceId(release.getMetadataSourceId());
        }
        if (release.getReleaseType() != null) {
            record.setReleaseType(toJooqReleaseType(release.getReleaseType()));
        }
        if (release.getFilePath() != null) {
            record.setFilePath(release.getFilePath());
        }
        if (release.getReleaseDate() != null) {
            record.setReleaseDate(release.getReleaseDate());
        }
    }

    @Override
    public boolean deleteById(long id) {
        int rowsDeleted = dsl.deleteFrom(RELEASE)
                .where(RELEASE.ID.eq(id))
                .execute();

        return rowsDeleted > 0;
    }
}
