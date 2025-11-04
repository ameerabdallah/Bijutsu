package com.ameerdev.metadata_agent.mangaupdates;

import com.ameerdev.model.media.SeriesMetadata;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Tag("integration")
class MangaUpdatesMetadataAgentIntegrationTest {

    @Inject
    MangaUpdatesMetadataAgent agent;

    @Test
    void searchByNameRealAPI() {
        Optional<String> result = agent.searchByName("Chainsaw-Man");

        assertTrue(result.isPresent(), "Should find Chainsaw Man series");
        System.out.println("Found series ID: " + result.get());
    }

    @Test
    void retrieveSeriesAPI() {
        Optional<SeriesMetadata> result = agent.fetchSeriesMetadata("75336092483");

        assertTrue(result.isPresent(), "Should find Chainsaw Man series");
        System.out.println("Found series ID: " + result.get());
    }
}