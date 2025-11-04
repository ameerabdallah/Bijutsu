package com.ameerdev.metadata_agent.mangaupdates;

import com.ameerdev.metadata_agent.mangaupdates.client.api.SeriesApi;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesModelV1;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesSearchRequestV1;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesSearchResponseV1;
import com.ameerdev.model.BookType;
import com.ameerdev.model.media.SeriesMetadata;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static com.ameerdev.test_util.MockUtils.loadMockResponseObject;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MangaUpdatesMetadataAgentTest {

    @InjectMock
    @RestClient
    SeriesApi seriesApi;

    @Inject
    MangaUpdatesMetadataAgent agent;

    @Test
    void metadataType() {
        assertEquals(BookType.MANGA, agent.metadataType());
    }

    @Test
    void searchByName() throws IOException {
        SeriesSearchResponseV1 mockResponse = loadMockResponseObject("mock-responses/series/search/chainsawman.json", SeriesSearchResponseV1.class);
        Optional<String> chainsawManId = Optional.of(mockResponse.getResults().getFirst().getRecord().getSeriesId().toString());
        when(seriesApi.searchSeriesPost(any(SeriesSearchRequestV1.class)))
                .thenReturn(mockResponse);

        Optional<String> result = agent.searchByName("Chainsaw Man");

        assertEquals(chainsawManId, result);
    }

    @Test
    void fetchSeriesMetadata() throws IOException {
        String metadataId = "75336092483";
        SeriesModelV1 mockResponse = loadMockResponseObject("mock-responses/series/id/chainsawman_seriesmetadata.json", SeriesModelV1.class);
        when(seriesApi.retrieveSeries(Long.valueOf(metadataId), false))
                .thenReturn(mockResponse);

        SeriesMetadata metadata = SeriesMetadata.builder()
                .title(mockResponse.getTitle())
                .metadataSourceId(metadataId)
                .description(mockResponse.getDescription())
                .build();

        SeriesMetadata result = agent.fetchSeriesMetadata(metadataId).orElseThrow();

        assertEquals(metadata.title(), result.title());
        assertEquals(metadata.metadataSourceId(), result.metadataSourceId());
        assertEquals(metadata.description(), result.description());
    }

    @Test
    void fetchReleaseMetadata() {
        agent.fetchReleaseMetadata("", 10);
    }
}