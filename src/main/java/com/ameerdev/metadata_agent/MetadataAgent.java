package com.ameerdev.metadata_agent;

import com.ameerdev.model.BookType;
import com.ameerdev.model.media.ReleaseMetadata;
import com.ameerdev.model.media.SeriesMetadata;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface MetadataAgent {
    @Nonnull
    BookType metadataType();

    /**
     * @param name expects an already parsed name
     * @return Metadata source's ID. If multiple results are found, return the result that best matches.
     */
    Optional<String> searchByName(String name);

    Optional<SeriesMetadata> fetchSeriesMetadata(String metadataSeriesId);

    default Optional<ReleaseMetadata> fetchReleaseMetadata(String metadataSeriesId, int index) {
        return Optional.of(ReleaseMetadata.builder().title("Chapter " + index).index(index).build());
    }
}
