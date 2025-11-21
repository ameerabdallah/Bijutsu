package com.ameerdev.repositories;

import com.ameerdev.models.Release;

import java.util.List;
import java.util.Optional;

public interface ReleaseRepository {
    /**
     * Retrieve all releases from the database.
     * @return a list of all Release objects
     */
    List<Release> findAll();

    /**
     * Retrieve a release by its ID.
     * @param id the ID of the release
     * @return an Optional containing the Release if found, or an empty Optional if not found
     */
    Optional<Release> findById(long id);

    /**
     * Retrieve all releases for a specific series.
     * @param seriesId the ID of the series
     * @return a list of releases for the given series
     */
    List<Release> findBySeriesId(long seriesId);

    /**
     * Retrieve a release by its metadata source ID.
     * @param metadataSourceId the metadata source ID
     * @return an Optional containing the Release if found, or an empty Optional if not found
     */
    Optional<Release> findByMetadataSourceId(String metadataSourceId);

    /**
     * Create a new release in the database.
     * @param release the release to be created
     * @return the created release wrapped in an Optional, or an empty Optional if creation failed
     */
    Optional<Release> create(Release release);

    /**
     * Update an existing release in the database.
     * @param release the release to be updated
     * @return the updated release wrapped in an Optional, or an empty Optional if update failed
     */
    Optional<Release> update(Release release);

    /**
     * Delete a release by its ID.
     * @param id the ID of the release to delete
     * @return true if the release was deleted, false otherwise
     */
    boolean deleteById(long id);
}
