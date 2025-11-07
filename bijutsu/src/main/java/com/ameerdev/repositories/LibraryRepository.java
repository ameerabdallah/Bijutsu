package com.ameerdev.repositories;

import com.ameerdev.jooq.tables.records.LibraryRecord;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository {
    List<LibraryRecord> getAllLibraries();
    List<String> getLibraryPaths(long libraryId);
    Optional<LibraryRecord> getLibraryById(long id);
}
