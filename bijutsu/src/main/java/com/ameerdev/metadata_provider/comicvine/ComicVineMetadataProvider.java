package com.ameerdev.metadata_provider.comicvine;

import com.ameerdev.metadata_provider.MetadataProvider;
import com.ameerdev.models.enums.BookType;
import com.ameerdev.models.Release;
import com.ameerdev.models.Series;
import com.ameerdev.models.SeriesIdentifier;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ComicVineMetadataProvider implements MetadataProvider {

    @Nonnull
    @Override
    public BookType metadataType() {
        return BookType.COMIC;
    }

    @Override
    public Optional<String> search(SeriesIdentifier name) {
        return Optional.empty();
    }

    @Override
    public Optional<Series> fetchSeriesMetadata(String metadataSeriesId) {
        return Optional.empty();
    }

    @Override
    public Optional<Release> fetchReleaseMetadata(String metadataSeriesId, int index) {
        return MetadataProvider.super.fetchReleaseMetadata(metadataSeriesId, index);
    }
}
