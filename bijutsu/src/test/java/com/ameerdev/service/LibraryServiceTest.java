package com.ameerdev.service;

import com.ameerdev.jooq.tables.pojos.Library;
import com.ameerdev.jooq.tables.records.LibraryRecord;
import com.ameerdev.repositories.LibraryRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@QuarkusTest
class LibraryServiceTest {

    @InjectMock
    LibraryRepository libraryRepository;

    @Inject
    LibraryService libraryService;

    @Test
    void performLibraryScan() {

    }

    @ExtendWith(MockitoExtension.class)
    public class LibraryRepositoryMock implements LibraryRepository {
        Map<Long, LibraryRecord> libraryData = new HashMap<>();
        Map<Long, List<String>> libraryPathsData = new HashMap<>();

        @Override
        public List<LibraryRecord> getAllLibraries() {
            return List.of();
        }

        @Override
        public List<String> getLibraryPaths(long libraryId) {

        }

        @Override
        public Optional<LibraryRecord> getLibraryById(long library) {
            LibraryRecord mockLibrary = new LibraryRecord();
            mockLibrary.setId(0L);
            mockLibrary.setName("Mock Library");
            return Optional.of(mockLibrary);
        }

        @Override
    }
}