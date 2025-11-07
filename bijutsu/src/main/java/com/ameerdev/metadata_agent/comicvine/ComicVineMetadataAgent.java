package com.ameerdev.metadata_agent.comicvine;

import com.ameerdev.jooq.enums.BookType;
import com.ameerdev.jooq.tables.pojos.Release;
import com.ameerdev.jooq.tables.pojos.Series;
import com.ameerdev.metadata_agent.MetadataAgent;
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
    public Optional<Series> fetchSeriesMetadata(String metadataSeriesId) {
        return Optional.empty();
    }

    @Override
    public Optional<Release> fetchReleaseMetadata(String metadataSeriesId, int index) {
        return MetadataAgent.super.fetchReleaseMetadata(metadataSeriesId, index);
    }
}
