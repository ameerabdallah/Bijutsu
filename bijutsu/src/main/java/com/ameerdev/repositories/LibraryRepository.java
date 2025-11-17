package com.ameerdev.repositories;

import com.ameerdev.models.Library;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository {
    /**
     * Retrieve all library records from the database.
     * @return a list of all LibraryRecord objects
     */
    List<Library> getAllLibraries();

    /**
     * Retrieve all paths associated with a specific library ID.
     * @param libraryId the ID of the library
     * @return a list of paths associated with the library
     */
    List<String> getLibraryPaths(long libraryId);

    /**
     * Retrieve a library record by its ID.
     * @param libraryId the ID of the library
     * @return an Optional containing the LibraryRecord if found, or an empty Optional if not found
     */
    Optional<Library> getLibraryById(long libraryId);

    /**
     * Create a new library record in the database. Also creates the necessary directory records.
     * @param libraryRecord the library record to be created
     * @param paths list of paths to be associated with the library
     * @return the created library record wrapped in an Optional, or an empty Optional if creation failed
     */
    Optional<Library> createLibrary(Library libraryRecord);
}
