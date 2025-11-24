package com.ameerdev.service;

import com.ameerdev.models.Series;
import com.ameerdev.repositories.SeriesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SeriesService {
    @Inject
    SeriesRepository seriesRepository;

    public List<Series> getAllSeries() {
        return seriesRepository.findAll();
    }

    public List<Series> getSeriesByLibraryId(long libraryId) {
        return seriesRepository.findSeriesByLibraryId(libraryId);
    }
}
