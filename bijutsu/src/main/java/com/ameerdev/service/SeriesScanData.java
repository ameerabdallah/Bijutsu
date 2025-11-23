package com.ameerdev.service;

import com.ameerdev.models.Series;

import java.util.List;
import java.util.Set;

public record SeriesScanData(
        Set<String> fileSystemSeriesPaths,
        List<Series> repoSeriesPaths
) {
}
