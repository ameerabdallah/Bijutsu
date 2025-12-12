package com.ameerdev.metadata_provider;

import com.ameerdev.metadata_provider.comicvine.ComicVineMetadataProvider;
import com.ameerdev.metadata_provider.mangaupdates.MangaUpdatesMetadataProvider;
import com.ameerdev.models.enums.BookType;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@QuarkusTest
public class MetadataProviderFactoryTest {
    @Inject
    MetadataProviderFactory testMetadataProviderFactory;

    @Test
    public void testDefaultMetadataProviderComics() {
        MetadataProvider metadataProvider = testMetadataProviderFactory.getDefaultProvider(BookType.COMIC);

        assertInstanceOf(ComicVineMetadataProvider.class, metadataProvider);
    }

    @Test
    public void testDefaultMetadataProviderManga() {
        MetadataProvider metadataProvider = testMetadataProviderFactory.getDefaultProvider(BookType.MANGA);

        assertInstanceOf(MangaUpdatesMetadataProvider.class, metadataProvider);
    }

    @Test
    public void testMetadataProviderCorrectMetadataType() {
        MetadataProvider mangaProvider = testMetadataProviderFactory.getDefaultProvider(BookType.MANGA);
        MetadataProvider comicsProvider = testMetadataProviderFactory.getDefaultProvider(BookType.COMIC);

        assertEquals(BookType.MANGA, mangaProvider.metadataType());
        assertEquals(BookType.COMIC, comicsProvider.metadataType());
    }
}
