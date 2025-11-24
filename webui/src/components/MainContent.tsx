import React, { useEffect, useState } from 'react';
import { useApp } from '../context/AppContext';
import { seriesApi, releaseApi } from '../services/api';
import type { Series, Release } from '../types';
import './MainContent.css';

const MainContent: React.FC = () => {
  const { selectedLibrary, selectedSeries, setSelectedSeries, viewMode } = useApp();
  const [series, setSeries] = useState<Series[]>([]);
  const [releases, setReleases] = useState<Release[]>([]);
  const [loading, setLoading] = useState(false);
  const [downloadingId, setDownloadingId] = useState<number | null>(null);

  useEffect(() => {
    if (selectedLibrary && viewMode === 'series') {
      loadSeries();
    }
  }, [selectedLibrary, viewMode]);

  useEffect(() => {
    if (selectedSeries && viewMode === 'releases') {
      loadReleases();
    }
  }, [selectedSeries, viewMode]);

  const loadSeries = async () => {
    if (!selectedLibrary) return;

    setLoading(true);
    try {
      const data = await seriesApi.getAllByLibrary(selectedLibrary.id);
      setSeries(data);
    } catch (error) {
      console.error('Failed to load series:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadReleases = async () => {
    if (!selectedSeries) return;

    setLoading(true);
    try {
      const data = await releaseApi.getAllBySeries(selectedSeries.id);
      setReleases(data);
    } catch (error) {
      console.error('Failed to load releases:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSeriesClick = (series: Series) => {
    setSelectedSeries(series);
  };

  const handleReleaseClick = async (release: Release) => {
    if (downloadingId) return; // Prevent multiple simultaneous downloads

    setDownloadingId(release.id);
    try {
      await releaseApi.download(release.id);
      // Download will start automatically, no need for alert
    } catch (error) {
      console.error('Failed to download release:', error);
      alert(`Failed to download ${release.title}`);
    } finally {
      setDownloadingId(null);
    }
  };

  const handleBackToSeries = () => {
    setSelectedSeries(null);
  };

  if (!selectedLibrary) {
    return (
      <div className="main-content">
        <div className="empty-state">
          <h2>Welcome to Bijutsu</h2>
          <p>Select a library from the sidebar to get started</p>
        </div>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="main-content">
        <div className="loading-state">Loading...</div>
      </div>
    );
  }

  if (viewMode === 'releases' && selectedSeries) {
    return (
      <div className="main-content">
        <div className="content-header">
          <button className="back-btn" onClick={handleBackToSeries}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M19 12H5M12 19l-7-7 7-7" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
            Back to Series
          </button>
          <h2>{selectedSeries.title}</h2>
        </div>

        {releases.length === 0 ? (
          <div className="empty-state">
            <p>No releases found for this series</p>
          </div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Title</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {releases.map((release) => (
                <tr
                  key={release.id}
                  onClick={() => handleReleaseClick(release)}
                  className={`clickable-row ${downloadingId === release.id ? 'downloading' : ''}`}
                >
                  <td>
                    {release.title}
                    {downloadingId === release.id && (
                      <span className="download-indicator"> (Downloading...)</span>
                    )}
                  </td>
                  <td>{release.releaseDate || 'N/A'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    );
  }

  return (
    <div className="main-content">
      <div className="content-header">
        <h2>Series</h2>
      </div>

      {series.length === 0 ? (
        <div className="empty-state">
          <p>No series found in this library</p>
          <p className="empty-state-hint">Try scanning the library from the sidebar menu</p>
        </div>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Description</th>
            </tr>
          </thead>
          <tbody>
            {series.map((s) => (
              <tr key={s.id} onClick={() => handleSeriesClick(s)} className="clickable-row">
                <td>{s.title}</td>
                <td>{s.description || 'No description available'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default MainContent;