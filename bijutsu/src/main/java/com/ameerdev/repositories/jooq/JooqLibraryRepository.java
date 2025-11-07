package com.ameerdev.repositories.jooq;

import com.ameerdev.jooq.tables.records.LibraryRecord;
import com.ameerdev.repositories.LibraryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

import static com.ameerdev.jooq.tables.Library.LIBRARY;

@ApplicationScoped
public class JooqLibraryRepository implements LibraryRepository {
    @Inject
    DSLContext dsl;

    @Override
    public List<LibraryRecord> getAllLibraries() {
        return dsl.selectFrom(LIBRARY)
                .fetch();
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
    public Optional<LibraryRecord> getLibraryById(long id) {
        return dsl.selectFrom(LIBRARY)
                .where(LIBRARY.ID.eq(id))
                .fetchOptional();
    }

}
