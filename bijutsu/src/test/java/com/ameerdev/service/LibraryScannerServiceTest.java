package com.ameerdev.service;

import com.ameerdev.metadata_provider.MetadataProvider;
import com.ameerdev.metadata_provider.MetadataProviderFactory;
import com.ameerdev.models.Series;
import com.ameerdev.repositories.LibraryRepository;
import com.ameerdev.repositories.ReleaseRepository;
import com.ameerdev.repositories.SeriesRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@QuarkusTest
class LibraryScannerServiceTest {

    private LibraryScannerService libraryScannerService;
    private MockedStatic<FileHandler> fileHandlerMock;

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private ReleaseRepository releaseRepository;

    @Mock
    private MetadataProviderFactory metadataProviderFactory;

    @Mock
    private MetadataProvider metadataProvider;

    @Mock
    private ParserService parserService;

    private ManagedExecutor managedExecutor;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        // Use a real executor for testing async behavior
        managedExecutor = mock(
                ManagedExecutor.class, invocation -> {
                    Runnable runnable = invocation.getArgument(0);
                    return CompletableFuture.runAsync(runnable, Executors.newCachedThreadPool());
                }
        );

        libraryScannerService = new LibraryScannerService();
        libraryScannerService.libraryRepository = libraryRepository;
        libraryScannerService.seriesRepository = seriesRepository;
        libraryScannerService.releaseRepository = releaseRepository;
        libraryScannerService.metadataProviderFactory = metadataProviderFactory;
        libraryScannerService.parserService = parserService;
        libraryScannerService.executor = managedExecutor;

        // Mock FileHandler static methods
        fileHandlerMock = mockStatic(FileHandler.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        fileHandlerMock.close();
        closeable.close();
    }

    @Test
    void scanLibrary_shouldNotScanIfLibraryNotFound() {
        // Given
        Long libraryId = 999L;
        when(libraryRepository.fetchLibraryById(libraryId)).thenReturn(Optional.empty());

        // When
        libraryScannerService.scanLibrary(libraryId);

        // Then: No series operations should occur
        verify(seriesRepository, never()).findSeriesByLibraryId(anyLong());
        verify(seriesRepository, never()).create(any(Series.class));
    }
}
