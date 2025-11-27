package com.ameerdev.repositories;

import com.ameerdev.models.Series;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SeriesRepository {
    /**
     * Retrieve all series from the database.
     *
     * @return a list of all Series objects
     */
    List<Series> findAll();

    /**
     * Retrieve all series for a specific library.
     *
     * @param libraryId the ID of the library
     * @return a list of series for the given library
     */
    List<Series> findSeriesByLibraryId(long libraryId);

    /**
     * Retrieve a series by its ID.
     *
     * @param id the ID of the series
     * @return an Optional containing the Series if found, or an empty Optional if not found
     */
    Optional<Series> fetchSeriesById(Long id);

    /**
     * Retrieve a series by its metadata source ID.
     *
     * @param metadataSourceId the metadata source ID
     * @return an Optional containing the Series if found, or an empty Optional if not found
     */
    Optional<Series> findSeriesByMetadataSourceId(String metadataSourceId);

    /**
     * Create a new series in the database.
     *
     * @param series the series to be created
     * @return the created series wrapped in an Optional, or an empty Optional if creation failed
     */
    Optional<Series> create(Series series);

    /**
     * Update an existing series in the database.
     *
     * @param series the series to be updated
     * @return the updated series wrapped in an Optional, or an empty Optional if update failed
     */
    Optional<Series> update(Series series);

    /**
     * Retrieve all series paths for a specific library.
     *
     * @param libraryId the ID of the library
     * @return a set of series paths for the given library
     */
    Set<String> findSeriesPathsByLibraryId(long libraryId);

    /**
     * Delete series by their paths.
     *
     * @param seriesPath a collection of series paths to delete
     */
    void deleteBySeriesPaths(Collection<String> seriesPath);

    /**
     * Delete series by their IDs.
     */
    void deleteByIds(Collection<Long> seriesIds);

    /**
     * Delete a series by its ID.
     *
     * @param id the ID of the series to delete
     * @return true if the series was deleted, false otherwise
     */
    boolean deleteById(long id);
}
