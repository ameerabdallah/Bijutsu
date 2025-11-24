package com.ameerdev.service;

import com.ameerdev.models.Library;
import com.ameerdev.repositories.LibraryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LibraryService {

    @Inject
    LibraryRepository repository;

    @Inject
    LibraryScannerService scanService;

    public Optional<Library> createNewLibrary(Library library) {
        Optional<Library> createdLibrary = repository.create(library);

        return createdLibrary.map(newLibrary -> {
            // we assume that the paths were created successfully along with the library
            scanService.scanLibrary(newLibrary);

            return newLibrary;
        });
    }

    public List<Library> getAllLibraries() {
        return repository.findAllLibraries();
    }

    public void deleteLibrary(long libraryId) {
        repository.deleteById(libraryId);
    }

    public void updateLibrary(long libraryId, @NotNull Library library) {
        repository.update(libraryId, library);
        // Always rescan the library after an update. Might not always be necessary, but
        // it's easier to keep things consistent this way
        scanService.scanLibrary(libraryId);
    }

    public Optional<Library> getLibraryById(long libraryId) {
        return repository.fetchLibraryById(libraryId);
    }
}
