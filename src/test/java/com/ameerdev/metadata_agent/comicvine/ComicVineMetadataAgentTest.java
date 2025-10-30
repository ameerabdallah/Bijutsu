package com.ameerdev.metadata_agent.comicvine;

import com.ameerdev.model.BookType;
import com.ameerdev.model.media.ReleaseMetadata;
import com.ameerdev.model.media.SeriesMetadata;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ComicVineMetadataAgentTest {
    @Inject
    ComicVineMetadataAgent metadataAgent;

    @Test
    void metadataType() {
        assertEquals(BookType.COMIC, metadataAgent.metadataType());
    }

    @Test
    void searchByName() throws IOException {
//        SeriesSearchResponseV1 mockResponse = loadMockResponseObject("mock-responses/series/search/batman.json", SeriesSearchResponseV1.class);
//        when(seriesApi.searchSeriesPost(any(SeriesSearchRequestV1.class)))
//                .thenReturn(mockResponse);
        String batmanId = "<placeholder>";

        String result = metadataAgent.searchByName("Batman").orElseThrow();

        assertEquals(batmanId, result);
    }

    @Test
    void fetchSeriesMetadata() throws IOException {
        String metadataId = "75336092483";
        SeriesMetadata metadata = SeriesMetadata.builder()
                .title("Batman")
                .build();

        SeriesMetadata result = metadataAgent.fetchSeriesMetadata(metadataId).orElseThrow();

        assertEquals(metadata.title(), result.title());
        assertEquals(metadata.metadataSourceId(), result.metadataSourceId());
        assertEquals(metadata.description(), result.description());
    }

    @Test
    void fetchReleaseMetadata() {
        String metadataId = "75336092483";
        ReleaseMetadata metadata = ReleaseMetadata.builder()
                .title("Batman #1000000 - Peril Within the Prison Planet")
                .build();

        ReleaseMetadata result = metadataAgent.fetchReleaseMetadata(metadataId, 1).orElseThrow();

        assertEquals(metadata.title(), result.title());
        assertEquals(metadata.metadataSourceId(), result.metadataSourceId());
    }
}