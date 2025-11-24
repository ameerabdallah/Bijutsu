package com.ameerdev.service;

import com.ameerdev.models.Release;
import com.ameerdev.repositories.ReleaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ReleaseService {
    @Inject
    ReleaseRepository releaseRepository;

    public List<Release> getReleasesBySeriesId(long seriesId) {
        return releaseRepository.findBySeriesId(seriesId);
    }

    public Optional<Path> getReleaseFilePath(long releaseId) {
        return releaseRepository.findById(releaseId)
                .map(Release::getFilePath)
                .map(Path::of);
    }
}
