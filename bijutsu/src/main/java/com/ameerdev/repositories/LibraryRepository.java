package com.ameerdev.repositories;

import com.ameerdev.models.Library;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository {
    /**
     * Retrieve all library records from the database.
     * @return a list of all LibraryRecord objects
     */
    List<Library> findAllLibraries();

    /**
     * Retrieve all paths associated with a specific library ID.
     * @param libraryId the ID of the library
     * @return a list of paths associated with the library
     */
    List<String> fetchLibraryPaths(long libraryId);

    /**
     * Retrieve a library record by its ID.
     * @param libraryId the ID of the library
     * @return an Optional containing the LibraryRecord if found, or an empty Optional if not found
     */
    Optional<Library> fetchLibraryById(long libraryId);

    /**
     * Create a new library record in the database. Also creates the necessary directory records.
     * @param library the library to be created
     * @return the created library record wrapped in an Optional, or an empty Optional if creation failed
     */
    Optional<Library> create(Library library);
}
