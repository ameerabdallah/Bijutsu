package com.ameerdev.metadata_provider;

import com.ameerdev.metadata_provider.comicvine.ComicVineMetadataProvider;
import com.ameerdev.metadata_provider.mangaupdates.MangaUpdatesMetadataProvider;
import com.ameerdev.models.enums.BookType;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MetadataProviderFactory {
    @Inject
    MangaUpdatesMetadataProvider mangaProvider;
    @Inject
    ComicVineMetadataProvider comicProvider;

    public @Nonnull MetadataProvider getDefaultProvider(BookType bookType) {
        return switch (bookType) {
            case MANGA -> mangaProvider;
            case COMIC -> comicProvider;
        };
    }
}