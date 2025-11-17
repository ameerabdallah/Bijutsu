package com.ameerdev.metadata_agent.mangaupdates;

import com.ameerdev.metadata_agent.MetadataAgent;
import com.ameerdev.metadata_agent.mangaupdates.client.api.SeriesApi;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesModelV1;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesSearchRequestV1;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesSearchResponseV1;
import com.ameerdev.metadata_agent.mangaupdates.client.model.SeriesSearchResponseV1Results;
import com.ameerdev.models.enums.BookType;
import com.ameerdev.models.Series;
import com.ameerdev.models.SeriesIdentifier;
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
    public Optional<String> search(SeriesIdentifier seriesIdentifier) {
        var builder = SeriesSearchRequestV1.builder();

        var searchRequest = builder
                .search(seriesIdentifier.getName())
                .year(seriesIdentifier.getYear().toString())
                .build();

        try {
            SeriesSearchResponseV1 response =
                    seriesApi.searchSeriesPost(searchRequest);

            return response.getResults().stream().map(SeriesSearchResponseV1Results::getRecord).min((record1,
                                                                                                     record2) -> {
                Integer record1Score = LevenshteinDistance.getDefaultInstance().apply(
                        record1.getTitle(),
                        seriesIdentifier.getName()
                );
                Integer record2Score = LevenshteinDistance.getDefaultInstance().apply(
                        record2.getTitle(),
                        seriesIdentifier.getName()
                );
                return record1Score.compareTo(record2Score);
            }).map(result -> result.getSeriesId().toString());

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Series> fetchSeriesMetadata(String metadataSeriesId) {
        SeriesModelV1 response = seriesApi.retrieveSeries(Long.valueOf(metadataSeriesId), false);

        return Optional.of(
                Series.builder()
                        .metadataSourceId(String.valueOf(response.getSeriesId()))
                        .title(response.getTitle())
                        .releaseYear(Integer.parseInt(response.getYear()))
                        .description(response.getDescription()).build());
    }
}
