package com.ameerdev.service;

import com.ameerdev.metadata_provider.MetadataProvider;
import com.ameerdev.metadata_provider.MetadataProviderFactory;
import com.ameerdev.models.*;
import com.ameerdev.models.enums.ReleaseType;
import com.ameerdev.repositories.LibraryRepository;
import com.ameerdev.repositories.ReleaseRepository;
import com.ameerdev.repositories.SeriesRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    ManagedExecutor executor;

    public void scanLibrary(Long libraryId) {
        Optional<Library> libraryOpt = libraryRepository.fetchLibraryById(libraryId);
        libraryOpt.ifPresent(this::scanLibrary);
    }

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
                CompletableFuture.runAsync(() -> handleExistingSeries(data, library), executor)
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

    private void handleExistingSeries(SeriesScanData scanData, Library library) {
        List<Series> existingSeriesPaths = scanData.repoSeriesPaths()
                .stream()
                .filter(series -> scanData.fileSystemSeriesPaths().contains(series.getPath()))
                .toList();

        for (Series series : existingSeriesPaths) {
            // For existing series, we might want to rescan or update metadata
            log.info("Existing series found at path: {}", series);
            scanSeries(library, series);
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

        seriesRepository.deleteByIds(seriesIdsToRemove);
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
        MetadataProvider metadataProvider = metadataProviderFactory.getDefaultProvider(library.getBookType());
        SeriesIdentifier seriesIdentifier = parserService.parseSeriesName(String.valueOf(path.getFileName()));
        Optional<String> metadataId = metadataProvider.search(seriesIdentifier);
        if (metadataId.isEmpty()) {
            log.warn("No metadata found for series at path: {}", path);
        }
        Optional<Series> series = seriesRepository.create(
                Series.builder()
                        .libraryId(library.getId())
                        .metadataSourceId(metadataId.orElse(null))
                        .path(path.toString())
                        .title(path.toString()) // Temporary title; will be updated after fetching metadata
                        .build()
        );

        series.ifPresent(s -> scanSeries(library, s));
    }

    private void scanSeries(Library library, Series series) {
        MetadataProvider metadataProvider = metadataProviderFactory.getDefaultProvider(library.getBookType());
        // Placeholder for scanning logic
        log.info(
                "Scanning {} in library ID: {} at path: {} with provider: {}",
                library.getSeriesType().displayName,
                library.getId(),
                series.getPath(),
                metadataProvider.getClass().getSimpleName()
        );

        // Check for series.json file first
        Optional<SeriesMetadataJson> localMetadata = parseSeriesJsonFile(series.getPath());

        // Fetch metadata from provider as fallback
        Optional<Series> providerMetadata = Optional.empty();
        if (series.getMetadataSourceId().isPresent()) {
            providerMetadata = metadataProvider.fetchSeriesMetadata(series.getMetadataSourceId().get());
        }

        // Merge metadata: prefer local series.json, fallback to provider
        Series updatedSeries = mergeSeriesMetadata(series, localMetadata, providerMetadata);
        seriesRepository.update(updatedSeries);

        // diff found files with existing releases
        Set<Path> files = findAllReleaseFiles(series);
        List<Release> existingReleases = releaseRepository.findBySeriesId(series.getId());

        List<Long> releasesToRemove = existingReleases.stream()
                .filter(release -> files.stream()
                        .noneMatch(file -> file.toString().equals(release.getFilePath()))
                )
                .map(Release::getId)
                .toList();

        releaseRepository.deleteByIds(releasesToRemove);

        Set<String> existingFilePaths = existingReleases.stream()
                .map(Release::getFilePath)
                .collect(Collectors.toSet());

        List<Path> newFiles = files.stream()
                .filter(file -> !existingFilePaths.contains(file.toString()))
                .toList();

        List<Release> newReleases = new ArrayList<>();
        for (Path file : newFiles) {
            Release release = Release.builder()
                    .seriesId(series.getId())
                    .releaseType(ReleaseType.CHAPTER)
                    .title(file.getFileName().toString())
                    .index(-1) // index is unknown at this point
                    .filePath(file.toString())
                    .build();

            newReleases.add(release);
        }
        newReleases = releaseRepository.createBatch(newReleases);

        existingReleases.addAll(newReleases);

        // re-index all releases
        existingReleases = existingReleases.stream()
                .sorted(Comparator.comparing(Release::getFilePath))
                .toList();

        for (int i = 0; i < existingReleases.size(); i++) {
            Release release = existingReleases.get(i);
            release.setIndex(i + 1);
            series.getMetadataSourceId().ifPresent(
                    id -> {
                        Optional<Release> metadataRelease = metadataProvider.fetchReleaseMetadata(
                                id,
                                release.getIndex()
                        );
                        if (metadataRelease.isPresent()) {
                            release.setTitle(metadataRelease.get().getTitle());
                            release.setMetadataSourceId(metadataRelease.get().getMetadataSourceId());
                        }
                    }
            );
        }

        releaseRepository.updateBatch(existingReleases);
    }

    private Set<Path> findAllReleaseFiles(Series series) {
        Set<Path> releaseFiles;
        try (Stream<Path> files = FileHandler.walk(Path.of(series.getPath()))) {
            releaseFiles = files
                    .filter(FileHandler::isRegularFile)
                    .filter(this::hasSupportedExtension)
                    .filter(file -> !FileHandler.isHidden(file))
                    .filter(FileHandler::isReadable)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return releaseFiles;
    }

    public boolean hasSupportedExtension(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        for (String ext : SUPPORTED_FILE_EXTENSIONS) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses the series.json file from the series directory if it exists.
     *
     * @param seriesPath The path to the series directory
     * @return Optional containing the parsed metadata, or empty if file doesn't exist or parsing fails
     */
    private Optional<SeriesMetadataJson> parseSeriesJsonFile(String seriesPath) {
        Path seriesJsonPath = Path.of(seriesPath, "series.json");

        if (!Files.exists(seriesJsonPath)) {
            log.debug("No series.json found at: {}", seriesJsonPath);
            return Optional.empty();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
            SeriesMetadataJson metadata = objectMapper.readValue(seriesJsonPath.toFile(), SeriesMetadataJson.class);
            log.info("Successfully parsed series.json at: {}", seriesJsonPath);
            return Optional.of(metadata);
        } catch (IOException e) {
            log.warn("Failed to parse series.json at: {}", seriesJsonPath, e);
            return Optional.empty();
        }
    }

    /**
     * Merges metadata from local series.json and metadata provider.
     * Prefers local metadata when available, falls back to provider metadata.
     *
     * @param series           The original series entity
     * @param localMetadata    Optional metadata from series.json file
     * @param providerMetadata Optional metadata from the metadata provider
     * @return Updated Series object with merged metadata
     */
    private Series mergeSeriesMetadata(Series series,
                                       Optional<SeriesMetadataJson> localMetadata,
                                       Optional<Series> providerMetadata) {
        if (providerMetadata.isPresent()) {
            Series providerSeries = providerMetadata.get();
            series.setTitle(providerSeries.getTitle());
            series.setDescription(providerSeries.getDescription());
            series.setReleaseYear(providerSeries.getReleaseYear());
            series.setAuthor(providerSeries.getAuthor());
        }
        if (localMetadata.isPresent()) {
            SeriesMetadataJson localMeta = localMetadata.get();
            SeriesMetadataJson.Metadata metadata = localMeta.getMetadata();

            if (metadata.getName() != null) {
                series.setTitle(metadata.getName());
            }
            if (metadata.getDescriptionText() != null) {
                series.setDescription(metadata.getDescriptionText());
            }
            if (metadata.getYear() != null) {
                series.setReleaseYear(metadata.getYear());
            }
            // Note: series.json schema doesn't have an author field, only publisher/imprint
            if (metadata.getPublisher() != null) {
                series.setAuthor(metadata.getPublisher());
            }
        }

        return series;
    }
}
