package com.ameerdev.service;

import com.ameerdev.metadata_agent.MetadataAgent;
import com.ameerdev.metadata_agent.MetadataAgentFactory;
import com.ameerdev.models.Series;
import com.ameerdev.models.enums.BookType;
import com.ameerdev.repositories.LibraryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ScannerService {

    private static final String[] SUPPORTED_FILE_EXTENSIONS = {".cbz", ".cbr", ".pdf", ".epub", ".zip", ".rar"};

    @Inject
    LibraryRepository repository;

    @Inject
    MetadataAgentFactory metadataAgentFactory;

    @Inject
    ParserService parserService;

    public void scanLibrary(long libraryId, List<String> paths, BookType libraryType) {
        log.info("Starting scan for library ID: {} and book type: {}", libraryId, libraryType);

        for (String path : paths) {
            this.scanSeries(libraryId, path, libraryType);
        }
    }

    private void scanSeries(long libraryId, String path, BookType libraryType) {
        MetadataAgent metadataAgent = metadataAgentFactory.getAgent(libraryType);
        // Placeholder for scanning logic
        log.info(
                "Scanning series in library ID: {} at path: {} with agent: {}",
                libraryId,
                path,
                metadataAgent.getClass().getSimpleName()
        );

        Path startPath = Paths.get(path);

        Series series = parserService.parseSeriesName(startPath.getFileName().toString());
    }


}
