package com.ameerdev.repositories;

import com.ameerdev.jooq.generated.tables.records.LibraryRecord;
import com.ameerdev.mapper.LibraryMapper;
import com.ameerdev.models.Library;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Optional;

import static com.ameerdev.jooq.generated.tables.Library.LIBRARY;
import static com.ameerdev.mapper.EnumConverters.BookTypeConverter.toJooqBookType;
import static com.ameerdev.mapper.EnumConverters.ReadDirectionConverter.toJooqReadDirection;
import static com.ameerdev.mapper.EnumConverters.SeriesTypeConverter.toJooqSeriesType;

@ApplicationScoped
public class LibraryRepositoryImpl implements LibraryRepository {
    @Inject
    DSLContext dsl;

    @Override
    public List<Library> getAllLibraries() {
        return dsl.select(LIBRARY.asterisk())
                .select(
                        DSL.multiset(
                                dsl.select(LIBRARY.libraryDirectory().PATH)
                                        .from(LIBRARY.libraryDirectory())
                                        .where(LIBRARY.libraryDirectory().LIBRARY_ID.eq(LIBRARY.ID))
                        ).as(LibraryMapper.PATHS_FIELD).convertFrom(r -> r.map(Record1::value1))
                )
                .from(LIBRARY)
                .fetchInto(Library.class);
    }

    @Override
    public List<String> getLibraryPaths(long libraryId) {
        return dsl.select(LIBRARY.libraryDirectory().PATH)
                .from(LIBRARY.libraryDirectory())
                .where(
                        LIBRARY.libraryDirectory().LIBRARY_ID.eq(libraryId)
                )
                .fetch()
                .getValues(LIBRARY.libraryDirectory().PATH);
    }

    @Override
    public Optional<Library> getLibraryById(long libraryId) {
        return dsl.selectFrom(LIBRARY)
                .where(LIBRARY.ID.eq(libraryId))
                .fetchOptionalInto(Library.class);
    }

    @Override
    public Optional<Library> createLibrary(Library library) {
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

                return Optional.of(recordResult.into(Library.class));
            }

            return Optional.empty();
        });
    }

}
