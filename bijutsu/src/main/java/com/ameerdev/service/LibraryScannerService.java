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
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
public class LibraryScannerService {

    private static final String[] SUPPORTED_FILE_EXTENSIONS = {".cbz", ".cbr", ".pdf", ".epub", ".zip", ".rar"};
    private final ConcurrentHashMap<Long, CompletableFuture<Void>> activeScans = new ConcurrentHashMap<>();
    @Inject
    LibraryRepository libraryRepository;
    @Inject
    SeriesRepository seriesRepository;
    @Inject
    ReleaseRepository releaseRepository;
    @Inject
    MetadataProviderFactory metadataProviderFactory;
    @Inject
    ParserService parserService;
    @Inject
    ManagedExecutor executor;

    public void scanLibrary(Library library) {
        Long libraryId = library.getId();

        CompletableFuture<Void> newScan = new CompletableFuture<>();
        CompletableFuture<Void> existingScan = activeScans.putIfAbsent(libraryId, newScan);
        if (existingScan != null) {
            log.warn("Scan already in progress for library: '{}' with ID={}", library.getName(), library.getId());
            return;
        }

        log.info("Starting scan for library: '{}' with ID={}", library.getName(), library.getId());

        CompletableFuture.supplyAsync(() -> fetchSeriesScanData(library), executor)
                .thenCompose(data -> runSeriesScans(library, data))
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error(
                                "Error occurred during scan for library: '{}' with ID={}",
                                library.getName(),
                                library.getId(),
                                ex
                        );
                        newScan.completeExceptionally(ex);
                    } else {
                        log.info("Completed scan for library: '{}' with ID={}", library.getName(), library.getId());
                        newScan.complete(null);
                    }
                    activeScans.remove(libraryId);
                });
    }

    private @NotNull CompletableFuture<Void> runSeriesScans(Library library, SeriesScanData data) {
        return CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> handleRemovedSeries(data), executor),
                CompletableFuture.runAsync(() -> handleNewSeries(data, library), executor),
                CompletableFuture.runAsync(() -> handleExistingSeries(data), executor)
        );
    }


    private @NotNull SeriesScanData fetchSeriesScanData(Library library) {
        Set<String> fileSystemSeriesPaths = new HashSet<>();
        for (String path : library.getPaths()) {
            fileSystemSeriesPaths.addAll(getSeriesPaths(Path.of(path)));
        }

        List<Series> repoSeriesPaths = seriesRepository.findSeriesByLibraryId(library.getId());

        return new SeriesScanData(fileSystemSeriesPaths, repoSeriesPaths);
    }

    private void handleExistingSeries(SeriesScanData scanData) {
        List<Series> existingSeriesPaths = scanData.repoSeriesPaths()
                .stream()
                .filter(series -> scanData.fileSystemSeriesPaths().contains(series.getPath()))
                .toList();

        for (Series seriesPath : existingSeriesPaths) {
            // For existing series, we might want to rescan or update metadata
            log.info("Existing series found at path: {}", seriesPath);
            // Placeholder for additional logic if needed
        }
    }

    private void handleNewSeries(SeriesScanData scanData, Library library) {
        List<String> newSeriesPaths = scanData.fileSystemSeriesPaths()
                .stream()
                .filter(path -> scanData.repoSeriesPaths()
                        .stream()
                        .noneMatch(series -> series.getPath().equals(path))
                )
                .toList();

        for (String seriesPath : newSeriesPaths) {
            scanSeries(library, Path.of(seriesPath));
        }
    }

    private void handleRemovedSeries(SeriesScanData scanData) {
        List<Long> seriesIdsToRemove = scanData.repoSeriesPaths()
                .stream()
                .filter(series -> !scanData.fileSystemSeriesPaths().contains(series.getPath()))
                .map(Series::getId)
                .toList();

        seriesRepository.deleteBySeriesIds(seriesIdsToRemove);
    }

    private Set<String> getSeriesPaths(Path of) {
        Set<String> seriesPaths = new HashSet<>();
        try (var paths = FileHandler.list(of)) {
            for (Path path : paths.toList()) {
                if (shouldSeriesPathBeVisible(path)) {
                    seriesPaths.add(path.toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return seriesPaths;
    }

    private boolean shouldSeriesPathBeVisible(Path path) {
        return FileHandler.isDirectory(path) && containsSupportedFileInDirectory(path);
    }

    private boolean containsSupportedFileInDirectory(Path directory) {
        try (var paths = FileHandler.list(directory)) {
            for (Path path : paths.toList()) {
                String fileName = path.getFileName().toString().toLowerCase();
                for (String extension : SUPPORTED_FILE_EXTENSIONS) {
                    if (fileName.endsWith(extension)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private void scanSeries(Library library, Path path) {
        MetadataProvider metadataProvider = metadataProviderFactory.getProvider(library.getBookType());
        // Placeholder for scanning logic
        log.info(
                "Scanning {} in library ID: {} at path: {} with provider: {}",
                library.getSeriesType().displayName,
                library.getId(),
                path,
                metadataProvider.getClass().getSimpleName()
        );

        SeriesIdentifier seriesIdentifier = parserService.parseSeriesName(String.valueOf(path.getFileName()));
        Optional<String> metadataId = metadataProvider.search(seriesIdentifier);
        if (metadataId.isEmpty()) {
            log.warn("No metadata found for series at path: {}", path);
        }
    }

    private void scanSeries(Library library, Series series) {
        MetadataProvider metadataProvider = metadataProviderFactory.getProvider(library.getBookType());
        // Placeholder for scanning logic
        log.info(
                "Scanning {} in library ID: {} at path: {} with provider: {}",
                library.getSeriesType().displayName,
                library.getId(),
                series.getPath(),
                metadataProvider.getClass().getSimpleName()
        );

        Optional<String> metadataId = metadataProvider.search(
                new SeriesIdentifier(
                        series.getMetadataSourceId(),
                        series.getTitle(),
                        series.getReleaseYear()
                )
        );

    }

}
