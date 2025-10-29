package com.ameerdev.metadata_agent.comicvine;

import com.ameerdev.metadata_agent.MetadataAgent;
import com.ameerdev.model.BookType;
import com.ameerdev.model.media.ReleaseMetadata;
import com.ameerdev.model.media.SeriesMetadata;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ComicVineMetadataAgent implements MetadataAgent {

    @Nonnull
    @Override
    public BookType metadataType() {
        return BookType.COMIC;
    }

    @Override
    public Optional<String> searchByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<SeriesMetadata> fetchSeriesMetadata(String metadataSeriesId) {
        return Optional.empty();
    }

    @Override
    public Optional<ReleaseMetadata> fetchReleaseMetadata(String metadataSeriesId, int index) {
        return MetadataAgent.super.fetchReleaseMetadata(metadataSeriesId, index);
    }
}
