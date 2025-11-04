package com.ameerdev.metadata_agent;

import com.ameerdev.metadata_agent.comicvine.ComicVineMetadataAgent;
import com.ameerdev.metadata_agent.mangaupdates.MangaUpdatesMetadataAgent;
import com.ameerdev.model.BookType;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class MetadataAgentFactoryTest {
    @Inject
    MetadataAgentFactory testMetadataAgentFactory;

    @Test
    public void testDefaultMetadataAgentComics() {
        MetadataAgent metadataAgent = testMetadataAgentFactory.getAgent(BookType.COMIC);

        assertInstanceOf(ComicVineMetadataAgent.class, metadataAgent);
    }

    @Test
    public void testDefaultMetadataAgentManga() {
        MetadataAgent metadataAgent = testMetadataAgentFactory.getAgent(BookType.MANGA);

        assertInstanceOf(MangaUpdatesMetadataAgent.class, metadataAgent);
    }

    @Test
    public void testMetadataAgentCorrectMetadataType() {
        MetadataAgent mangaAgent = testMetadataAgentFactory.getAgent(BookType.MANGA);
        MetadataAgent comicsAgent = testMetadataAgentFactory.getAgent(BookType.COMIC);

        assertEquals(BookType.MANGA, mangaAgent.metadataType());
        assertEquals(BookType.COMIC, comicsAgent.metadataType());
    }
}
