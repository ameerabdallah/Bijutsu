package com.ameerdev.repositories;

import com.ameerdev.jooq.tables.pojos.Series;

import java.util.List;

public interface SeriesRepository {
    List<Series> findSeriesByLibraryID();
}
