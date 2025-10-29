package com.ameerdev.model.repositories.media;

import com.ameerdev.model.media.Series;

import java.util.List;

public interface SeriesRepository {
    List<Series> findSeriesByLibraryID();
}
