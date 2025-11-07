package com.ameerdev.service;

import com.ameerdev.repositories.LibraryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LibraryService {

    @Inject
    LibraryRepository repository;

    void performLibraryScan(int libraryId) {

    }
}
