package com.ameerdev.repositories;

import com.ameerdev.models.Series;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository {
    List<Series> findSeriesByLibraryID();
    Optional<Series> fetchSeriesById(Long id);
    Optional<Series> fetchSeriesByMetadataSourceId(String metadataSourceId);
}
