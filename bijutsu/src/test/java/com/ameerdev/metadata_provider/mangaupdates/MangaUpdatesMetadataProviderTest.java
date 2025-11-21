package com.ameerdev.metadata_provider.mangaupdates;

import com.ameerdev.metadata_provider.mangaupdates.client.api.SeriesApi;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesModelV1;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesSearchRequestV1;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesSearchResponseV1;
import com.ameerdev.models.Series;
import com.ameerdev.models.SeriesIdentifier;
import com.ameerdev.models.enums.BookType;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static com.ameerdev.test_util.MockUtils.loadMockResponseObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class MangaUpdatesMetadataProviderTest {

    @InjectMock
    @RestClient
    SeriesApi seriesApi;

    @Inject
    MangaUpdatesMetadataProvider provider;

    @Test
    void metadataType() {
        assertEquals(BookType.MANGA, provider.metadataType());
    }

    @Test
    void search() throws IOException {
        SeriesSearchResponseV1 mockResponse = loadMockResponseObject(
                "mock-responses/series/search/chainsawman.json",
                SeriesSearchResponseV1.class
        );
        Optional<String> chainsawManId =
                Optional.of(mockResponse.getResults().getFirst().getRecord().getSeriesId().toString());
        when(seriesApi.searchSeriesPost(any(SeriesSearchRequestV1.class))).thenReturn(mockResponse);

        Optional<String> result = provider.search(SeriesIdentifier.builder().title("Chainsaw-Man").build());

        assertEquals(chainsawManId, result);
    }

    @Test
    void searchWithYear() throws IOException {
        SeriesSearchResponseV1 mockResponse = loadMockResponseObject(
                "mock-responses/series/search/chainsawman+year.json",
                SeriesSearchResponseV1.class
        );
        Optional<String> chainsawManId =
                Optional.of(mockResponse.getResults().getFirst().getRecord().getSeriesId().toString());
        when(seriesApi.searchSeriesPost(any(SeriesSearchRequestV1.class))).thenReturn(mockResponse);

        Optional<String> result = provider.search(SeriesIdentifier.builder().title("Chainsaw-Man").year(2018).build());

        assertEquals(chainsawManId, result);
    }

    @Test
    void fetchSeriesMetadata() throws IOException {
        String metadataId = "75336092483";
        SeriesModelV1 mockResponse = loadMockResponseObject(
                "mock-responses/series/id/chainsawman_seriesmetadata.json",
                SeriesModelV1.class
        );
        when(seriesApi.retrieveSeries(Long.valueOf(metadataId), false)).thenReturn(mockResponse);

        Series expected = Series.builder()
                .title(mockResponse.getTitle())
                .metadataSourceId(metadataId)
                .description(mockResponse.getDescription())
                .build();

        Series result = provider.fetchSeriesMetadata(metadataId).orElseThrow();

        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getMetadataSourceId(), result.getMetadataSourceId());
        assertEquals(expected.getDescription(), result.getDescription());
    }

    @Test
    void fetchReleaseMetadata() {
        provider.fetchReleaseMetadata("", 10);
    }
}