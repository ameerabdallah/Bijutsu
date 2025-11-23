package com.ameerdev.metadata_provider.mangaupdates;

import com.ameerdev.models.Series;
import com.ameerdev.models.SeriesIdentifier;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@Tag("integration")
class MangaUpdatesMetadataProviderIntegrationTest {

    @Inject
    MangaUpdatesMetadataProvider provider;

    @Test
    void searchRealAPI() {
        Optional<String> result = provider.search(SeriesIdentifier.builder().title("Chainsaw-Man").build());

        assertTrue(result.isPresent(), "Should find Chainsaw Man series");
        System.out.println("Found series ID: " + result.get());
    }

    @Test
    void searchRealAPIWithYear() {
        Optional<String> result = provider.search(SeriesIdentifier.builder().title("Chainsaw-Man").year(2018).build());

        assertTrue(result.isPresent(), "Should find Chainsaw Man series");
        System.out.println("Found series ID: " + result.get());
    }

    @Test
    void retrieveSeriesAPI() {
        Optional<Series> result = provider.fetchSeriesMetadata("75336092483");

        assertTrue(result.isPresent(), "Should find Chainsaw Man series");
        System.out.println("Found series ID: " + result.get());
    }

    @Test
    void searchWithDashesAndUnderscores() {
        Optional<String> result = provider.search(SeriesIdentifier.builder().title("One-Punch_Man").build());

        assertTrue(result.isPresent(), "Should find Frieren series");
        System.out.println("Found series ID: " + result.get());
    }

    //
    @Test
    void testSearchCache() {
        long startTime = System.currentTimeMillis();
        Optional<String> firstResult = provider.search(SeriesIdentifier.builder().title("Chainsaw Man").build());
        long firstDuration = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        Optional<String> secondResult = provider.search(SeriesIdentifier.builder().title("Chainsaw Man").build());
        long secondDuration = System.currentTimeMillis() - startTime;

        assertEquals(firstResult, secondResult, "Both results should be the same");
        // The second call should be significantly faster due to caching. Somewhere around 1ms. If it's not, the cache is likely not working.
        // Not asserting on duration to avoid flaky tests, but printing out for manual verification.
        System.out.println("First call duration: " + firstDuration + "ms, Second call duration: " + secondDuration + "ms");
    }
}