package com.ameerdev.metadata_provider;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "metadata")
public interface MetadataConfig {
    String comicProvider();
    String mangaProvider();
}
