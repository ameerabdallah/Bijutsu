package com.ameerdev.repositories.jooq;

import com.ameerdev.jooq.generated.tables.records.LibraryRecord;
import com.ameerdev.models.Library;
import com.ameerdev.repositories.LibraryRepository;
import com.ameerdev.repositories.jooq.mapper.LibraryMapper;
import com.ameerdev.repositories.jooq.mapper.LibraryWithPathsMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Optional;

import static com.ameerdev.jooq.generated.tables.Library.LIBRARY;
import static com.ameerdev.repositories.jooq.mapper.EnumConverters.BookTypeConverter.toJooqBookType;
import static com.ameerdev.repositories.jooq.mapper.EnumConverters.ReadDirectionConverter.toJooqReadDirection;
import static com.ameerdev.repositories.jooq.mapper.EnumConverters.SeriesTypeConverter.toJooqSeriesType;

@ApplicationScoped
public class LibraryRepositoryImpl implements LibraryRepository {
    @Inject
    DSLContext dsl;

    @Override
    public List<Library> findAllLibraries() {
        return dsl.select(LIBRARY.asterisk())
                .select(
                        DSL.multiset(
                                dsl.select(LIBRARY.libraryDirectory().PATH)
                                        .from(LIBRARY.libraryDirectory())
                                        .where(LIBRARY.libraryDirectory().LIBRARY_ID.eq(LIBRARY.ID))
                        ).as(LibraryWithPathsMapper.PATHS_FIELD).convertFrom(r -> r.map(Record1::value1))
                )
                .from(LIBRARY)
                .fetchInto(Library.class);
    }

    @Override
    public List<String> fetchLibraryPaths(long libraryId) {
        return dsl.select(LIBRARY.libraryDirectory().PATH)
                .from(LIBRARY.libraryDirectory())
                .where(
                        LIBRARY.libraryDirectory().LIBRARY_ID.eq(libraryId)
                )
                .fetch()
                .getValues(LIBRARY.libraryDirectory().PATH);
    }

    @Override
    public Optional<Library> fetchLibraryById(long libraryId) {
        return dsl.select(LIBRARY.asterisk())
                .select(
                        DSL.multiset(
                                dsl.select(LIBRARY.libraryDirectory().PATH)
                                        .from(LIBRARY.libraryDirectory())
                                        .where(LIBRARY.libraryDirectory().LIBRARY_ID.eq(LIBRARY.ID))
                        ).as(LibraryWithPathsMapper.PATHS_FIELD).convertFrom(r -> r.map(Record1::value1))
                )
                .from(LIBRARY)
                .where(LIBRARY.ID.eq(libraryId))
                .fetchOptional()
                .map(record -> record.into(Library.class));
    }

    @Override
    public Optional<Library> create(Library library) {
        return dsl.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            LibraryRecord record = ctx.newRecord(LIBRARY);
            record.setDescription(library.getDescription());
            record.setName(library.getName());
            record.setSeriesType(toJooqSeriesType(library.getSeriesType()));
            record.setBookType(toJooqBookType(library.getBookType()));
            record.setReadDirection(toJooqReadDirection(library.getReadDirection()));

            LibraryRecord recordResult = ctx.insertInto(LIBRARY)
                    .set(record)
                    .returning()
                    .fetchAny();

            if (recordResult != null) {
                for (String path : library.getPaths()) {
                    ctx.insertInto(LIBRARY.libraryDirectory())
                            .set(LIBRARY.libraryDirectory().LIBRARY_ID, recordResult.getId())
                            .set(LIBRARY.libraryDirectory().PATH, path)
                            .execute();
                }

                // TODO: Map to Library.class directly
                return Optional.of(recordResult.map(new LibraryMapper(library.getPaths())));
            }

            return Optional.empty();
        });
    }

    @Override
    public void deleteById(long libraryId) {
        dsl.deleteFrom(LIBRARY)
                .where(LIBRARY.ID.eq(libraryId))
                .execute();
    }

    @Override
    public void update(long libraryId, @NotNull Library library) {
        dsl.transaction(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            LibraryRecord record = ctx.newRecord(LIBRARY);
            record.setDescription(library.getDescription());
            record.setName(library.getName());
            record.setSeriesType(toJooqSeriesType(library.getSeriesType()));
            record.setBookType(toJooqBookType(library.getBookType()));
            record.setReadDirection(toJooqReadDirection(library.getReadDirection()));

            ctx.update(LIBRARY)
                    .set(record)
                    .where(LIBRARY.ID.eq(libraryId))
                    .execute();

            // Update library paths
            ctx.deleteFrom(LIBRARY.libraryDirectory())
                    .where(LIBRARY.libraryDirectory().LIBRARY_ID.eq(libraryId))
                    .execute();

            for (String path : library.getPaths()) {
                ctx.insertInto(LIBRARY.libraryDirectory())
                        .set(LIBRARY.libraryDirectory().LIBRARY_ID, libraryId)
                        .set(LIBRARY.libraryDirectory().PATH, path)
                        .execute();
            }
        });
    }
}