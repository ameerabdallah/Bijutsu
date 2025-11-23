package com.ameerdev.metadata_provider.mangaupdates;

import com.ameerdev.metadata_provider.mangaupdates.client.api.SeriesApi;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesModelV1;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesSearchRequestV1;
import com.ameerdev.metadata_provider.mangaupdates.client.model.SeriesSearchResponseV1;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class MangaUpdatesMetadataProxy {
    @RestClient
    @Inject
    SeriesApi seriesApi;

    @CacheResult(cacheName = "mangaupdates-series-cache")
    public SeriesModelV1 getSeriesById(Long seriesId) {
        return seriesApi.retrieveSeries(seriesId, false);
    }

    @CacheResult(cacheName = "mangaupdates-series-search-cache")
    public SeriesSearchResponseV1 searchSeries(String title, String year) {
        return seriesApi.searchSeriesPost(
                SeriesSearchRequestV1.builder()
                        .search(title)
                        .year(year)
                        .build()
        );
    }

}
