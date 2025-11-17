package com.ameerdev.service;

import com.ameerdev.repositories.LibraryRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class LibraryScanTest {

    @InjectMock
    LibraryRepository libraryRepository;

    @Inject
    LibraryService libraryService;

    @Test
    void performLibraryScan() {

        libraryService.performLibraryScan(1L);
    }

}