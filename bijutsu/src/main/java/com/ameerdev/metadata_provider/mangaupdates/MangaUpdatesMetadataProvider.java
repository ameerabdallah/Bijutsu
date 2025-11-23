package com.ameerdev.metadata_provider.mangaupdates;

import com.ameerdev.metadata_provider.MetadataProvider;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesModelV1;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesSearchRequestV1;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesSearchResponseV1;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesSearchResponseV1Results;
import com.ameerdev.models.Series;
import com.ameerdev.models.SeriesIdentifier;
import com.ameerdev.models.enums.BookType;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ApplicationScoped
public class MangaUpdatesMetadataProvider implements MetadataProvider {

    private static final Logger log = LoggerFactory.getLogger(MangaUpdatesMetadataProvider.class);

    @Inject
    MangaUpdatesMetadataProxy proxy;

    @Nonnull
    @Override
    public BookType metadataType() {
        return BookType.MANGA;
    }

    @Override
    public Optional<String> search(SeriesIdentifier seriesIdentifier) {
        var builder = SeriesSearchRequestV1.builder();

        if (seriesIdentifier.getMetadataSourceId().isPresent()) {
            var metadata = proxy.getSeriesById(
                    Long.valueOf(seriesIdentifier.getMetadataSourceId().get())
            );

            if (metadata != null) {
                return Optional.of(String.valueOf(metadata.getSeriesId()));
            }
            log.info("Metadata source ID '{}' not found in MangaUpdates", seriesIdentifier.getMetadataSourceId().get());
        }

        if (seriesIdentifier.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title or metadata id is required to search for series metadata id");
        }

        try {
            var title = seriesIdentifier.getTitle().get().replaceAll("_", " ");
            SeriesSearchResponseV1 response =
                    proxy.searchSeries(
                            title,
                            seriesIdentifier.getYear().map(String::valueOf).orElse(null)
                    );

            return response.getResults().stream().map(SeriesSearchResponseV1Results::getRecord).min((record1,
                                                                                                     record2) -> {
                Integer record1Score = LevenshteinDistance.getDefaultInstance().apply(
                        record1.getTitle(),
                        title
                );
                Integer record2Score = LevenshteinDistance.getDefaultInstance().apply(
                        record2.getTitle(),
                        title
                );
                return record1Score.compareTo(record2Score);
            }).map(result -> result.getSeriesId().toString());

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Series> fetchSeriesMetadata(String metadataSeriesId) {
        SeriesModelV1 response = proxy.getSeriesById(Long.valueOf(metadataSeriesId));

        return Optional.of(
                Series.builder()
                        .metadataSourceId(String.valueOf(response.getSeriesId()))
                        .title(response.getTitle())
                        .releaseYear(Integer.parseInt(response.getYear()))
                        .description(response.getDescription()).build()
        );
    }
}
