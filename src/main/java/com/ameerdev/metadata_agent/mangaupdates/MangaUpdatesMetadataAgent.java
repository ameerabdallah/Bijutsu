package com.ameerdev.metadata_agent.mangaupdates;

import com.ameerdev.metadata_agent.MetadataAgent;
import com.ameerdev.metadata_agent.mangaupdates.client.api.SeriesApi;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesModelV1;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesSearchRequestV1;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesSearchResponseV1;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesSearchResponseV1Results;
import com.ameerdev.model.BookType;
import com.ameerdev.model.media.ReleaseMetadata;
import com.ameerdev.model.media.SeriesMetadata;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;

@ApplicationScoped
public class MangaUpdatesMetadataAgent implements MetadataAgent {

    @RestClient
    @Inject
    SeriesApi seriesApi;

    @Nonnull
    @Override
    public BookType metadataType() {
        return BookType.MANGA;
    }

    @Override
    public Optional<String> searchByName(String name) {
        SeriesSearchRequestV1.SeriesSearchRequestV1Builder builder = SeriesSearchRequestV1.builder();

        try {
            SeriesSearchResponseV1 response = seriesApi.searchSeriesPost(builder.search(name).build());

            return response.getResults().stream()
                    .map(SeriesSearchResponseV1Results::getRecord)
                    .min((record1, record2) -> {
                        Integer record1Score = LevenshteinDistance.getDefaultInstance().apply(record1.getTitle(), name);
                        Integer record2Score = LevenshteinDistance.getDefaultInstance().apply(record2.getTitle(), name);
                        return record1Score.compareTo(record2Score);
                    })
                    .map(result -> result.getSeriesId().toString());

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SeriesMetadata> fetchSeriesMetadata(String metadataSeriesId) {
        SeriesModelV1 response = seriesApi.retrieveSeries(Long.valueOf(metadataSeriesId), false);

        return Optional.of(
                SeriesMetadata.builder()
                        .metadataSourceId(String.valueOf(response.getSeriesId()))
                        .title(response.getTitle())
                        // TODO: Add release date
                        .description(response.getDescription())
                        .build()
        );
    }

    @Override
    public Optional<ReleaseMetadata> fetchReleaseMetadata(String metadataSeriesId, int index) {
//        seriesApi.searchSeriesPost()
        return Optional.empty();
    }
}
