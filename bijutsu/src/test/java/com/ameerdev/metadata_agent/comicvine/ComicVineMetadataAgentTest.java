package com.ameerdev.metadata_agent.comicvine;

import com.ameerdev.jooq.enums.BookType;
import com.ameerdev.jooq.tables.pojos.Release;
import com.ameerdev.jooq.tables.pojos.Series;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Disabled
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
    void fetchSeriesMetadata() {
        String metadataId = "75336092483";
        Series metadata = new Series()
                .setTitle("Batman");

        Series result = metadataAgent.fetchSeriesMetadata(metadataId).orElseThrow();

        assertEquals(metadata.getTitle(), result.getTitle());
        assertEquals(metadata.getMetadataSourceId(), result.getMetadataSourceId());
        assertEquals(metadata.getDescription(), result.getDescription());
    }

    @Test
    void fetchReleaseMetadata() {
        String metadataId = "75336092483";
        Release metadata = new Release()
                .setTitle("Batman #1000000 - Peril Within the Prison Planet");

        Release result = metadataAgent.fetchReleaseMetadata(metadataId, 1).orElseThrow();

        assertEquals(metadata.getTitle(), result.getTitle());
        assertEquals(metadata.getMetadataSourceId(), result.getMetadataSourceId());
    }
}