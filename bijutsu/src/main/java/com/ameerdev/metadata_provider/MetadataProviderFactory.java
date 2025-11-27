package com.ameerdev.metadata_provider;

import com.ameerdev.metadata_provider.comicvine.ComicVineMetadataProvider;
import com.ameerdev.metadata_provider.mangaupdates.MangaUpdatesMetadataProvider;
import com.ameerdev.models.enums.BookType;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class MetadataProviderFactory {
    private final Map<BookType, MetadataProvider> providerMap;

    MetadataProviderFactory() {
        providerMap = Map.of(
                BookType.COMIC, new ComicVineMetadataProvider(),
                BookType.MANGA, new MangaUpdatesMetadataProvider()
        );
    }

    public @Nonnull MetadataProvider getDefaultProvider(BookType bookType) {
        return providerMap.get(bookType);
    }
}
