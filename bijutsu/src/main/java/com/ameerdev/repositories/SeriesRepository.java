package com.ameerdev.repositories;

import com.ameerdev.jooq.generated.tables.pojos.Series;

import java.util.List;

public interface SeriesRepository {
    List<Series> findSeriesByLibraryID();
}
