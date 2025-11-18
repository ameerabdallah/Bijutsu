package com.ameerdev.metadata_agent.mangaupdates;

import com.ameerdev.models.Series;
import com.ameerdev.models.SeriesIdentifier;
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
    void searchRealAPI() {
        Optional<String> result = agent.search(SeriesIdentifier.builder().title("Chainsaw-Man").build());

        assertTrue(result.isPresent(), "Should find Chainsaw Man series");
        System.out.println("Found series ID: " + result.get());
    }

    @Test
    void searchRealAPIWithYear() {
        Optional<String> result = agent.search(SeriesIdentifier.builder().title("Chainsaw-Man").year(2018).build());

        assertTrue(result.isPresent(), "Should find Chainsaw Man series");
        System.out.println("Found series ID: " + result.get());
    }

    @Test
    void retrieveSeriesAPI() {
        Optional<Series> result = agent.fetchSeriesMetadata("75336092483");

        assertTrue(result.isPresent(), "Should find Chainsaw Man series");
        System.out.println("Found series ID: " + result.get());
    }
}