package com.ameerdev.service;

import com.ameerdev.models.Library;
import com.ameerdev.repositories.LibraryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class LibraryService {

    @Inject
    LibraryRepository repository;

    @Inject
    LibraryScannerService scanService;

    public void performLibraryScan(long libraryId) {
        Optional<Library> fetchedLibrary = repository.fetchLibraryById(libraryId);

        fetchedLibrary.ifPresent(library -> {
            scanService.scanLibrary(library);
        });
    }

    public Optional<Library> createNewLibrary(Library library) {
        Optional<Library> createdLibrary = repository.create(library);

        return createdLibrary.map(newLibrary -> {
            // we assume that the paths were created successfully along with the library
            scanService.scanLibrary(newLibrary);

            return newLibrary;
        });
    }
}
