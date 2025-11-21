package com.ameerdev.metadata_provider;

import com.ameerdev.metadata_provider.comicvine.ComicVineMetadataProvider;
import com.ameerdev.metadata_provider.mangaupdates.MangaUpdatesMetadataProvider;
import com.ameerdev.models.enums.BookType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.TreeMap;

@ApplicationScoped
public class MetadataProviderFactory {
    @Inject
    MetadataConfig config;

    @Inject
    Instance<MetadataProvider> providers;

    private final Map<BookType, MetadataProvider> providerMap = new TreeMap<>();

    @PostConstruct
    public void init() {
        providers.stream()
                .filter(provider -> provider.metadataType() == BookType.COMIC)
                .filter(provider -> provider.getClass().getSimpleName()
                        .toLowerCase().contains(config.comicProvider().toLowerCase()))
                .findFirst()
                .ifPresent(provider -> providerMap.put(BookType.COMIC, provider));

        providers.stream()
                .filter(provider -> provider.metadataType() == BookType.MANGA)
                .filter(provider -> provider.getClass().getSimpleName()
                        .toLowerCase().contains(config.mangaProvider().toLowerCase()))
                .findFirst()
                .ifPresent(provider -> providerMap.put(BookType.MANGA, provider));

        // Defaults
        providerMap.computeIfAbsent(BookType.COMIC, k -> new ComicVineMetadataProvider());
        providerMap.computeIfAbsent(BookType.MANGA, k -> new MangaUpdatesMetadataProvider());
    }

    public @Nonnull MetadataProvider getProvider(BookType bookType) {
        return providerMap.get(bookType);
    }
}
