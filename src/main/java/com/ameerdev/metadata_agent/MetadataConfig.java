package com.ameerdev.metadata_agent;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "metadata")
public interface MetadataConfig {
    String comicAgent();
    String mangaAgent();
}
