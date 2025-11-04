package com.ameerdev.metadata_agent;

import com.ameerdev.metadata_agent.comicvine.ComicVineMetadataAgent;
import com.ameerdev.metadata_agent.mangaupdates.MangaUpdatesMetadataAgent;
import com.ameerdev.model.BookType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.TreeMap;

@ApplicationScoped
public class MetadataAgentFactory {
    @Inject
    MetadataConfig config;

    @Inject
    Instance<MetadataAgent> agents;

    private final Map<BookType, MetadataAgent> agentMap = new TreeMap<>();

    @PostConstruct
    public void init() {
        agents.stream()
                .filter(agent -> agent.metadataType() == BookType.COMIC)
                .filter(agent -> agent.getClass().getSimpleName()
                        .toLowerCase().contains(config.comicAgent().toLowerCase()))
                .findFirst()
                .ifPresent(agent -> agentMap.put(BookType.COMIC, agent));

        agents.stream()
                .filter(agent -> agent.metadataType() == BookType.MANGA)
                .filter(agent -> agent.getClass().getSimpleName()
                        .toLowerCase().contains(config.mangaAgent().toLowerCase()))
                .findFirst()
                .ifPresent(agent -> agentMap.put(BookType.MANGA, agent));

        // Defaults
        agentMap.computeIfAbsent(BookType.COMIC, k -> new ComicVineMetadataAgent());
        agentMap.computeIfAbsent(BookType.MANGA, k -> new MangaUpdatesMetadataAgent());
    }

    public @Nonnull MetadataAgent getAgent(BookType libraryType) {
        return agentMap.get(libraryType);
    }
}
