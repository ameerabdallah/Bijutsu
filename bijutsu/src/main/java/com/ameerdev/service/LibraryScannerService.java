package com.ameerdev.service;

import com.ameerdev.metadata_provider.MetadataProvider;
import com.ameerdev.metadata_provider.MetadataProviderFactory;
import com.ameerdev.models.Library;
import com.ameerdev.models.Series;
import com.ameerdev.models.SeriesIdentifier;
import com.ameerdev.repositories.LibraryRepository;
import com.ameerdev.repositories.ReleaseRepository;
import com.ameerdev.repositories.SeriesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@ApplicationScoped
public class LibraryScannerService {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private static final String[] SUPPORTED_FILE_EXTENSIONS = {".cbz", ".cbr", ".pdf", ".epub", ".zip", ".rar"};

    @Inject
    LibraryRepository libraryRepository;

    @Inject
    SeriesRepository seriesRepository;

    @Inject
    ReleaseRepository releaseRepository;

    @Inject
    MetadataProviderFactory metadataProviderFactory;

    @Inject
    SeriesParserService seriesParserService;

    public void scanLibrary(Library library) {
        log.info("Starting scan for library: '{}' with ID={}", library.getName(), library.getId());

        for (String path : library.getPaths()) {
            executorService.submit(() -> this.scanLibraryPath(library, Path.of(path)));
        }
    }

    public void scanLibraryPath(Library library, Path path) {
        MetadataProvider metadataProvider = metadataProviderFactory.getProvider(library.getBookType());

        // get each subdirectory in the path and scan it as a series
        List<Path> subDirectories;
        try (var paths = Files.list(path)) {
            subDirectories = paths.parallel().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Path subDirectory : subDirectories) {
            if (Files.isDirectory(subDirectory)) {
                SeriesIdentifier seriesIdentifier = seriesParserService.parseSeriesName(subDirectory.getFileName().toString());

                // If the parser was able to extract a metadata source ID, use it to look up the series
                if (seriesIdentifier.getMetadataSourceId() != null) {
                    seriesRepository.fetchSeriesByMetadataSourceId(seriesIdentifier.getMetadataSourceId()).ifPresent(
                            series -> this.scanSeries(
                                    library,
                                    series,
                                    subDirectory
                            )
                    );
                }

                String metadataSourceId = metadataProvider.search(seriesIdentifier);

            }
        }
    }

    private void scanSeries(Library library, Series series, Path path) {
        MetadataProvider metadataProvider = metadataProviderFactory.getProvider(library.getBookType());
        // Placeholder for scanning logic
        log.info(
                "Scanning {} in library ID: {} at path: {} with provider: {}",
                library.getSeriesType().displayName,
                library.getId(),
                path,
                metadataProvider.getClass().getSimpleName()
        );

    }


}
