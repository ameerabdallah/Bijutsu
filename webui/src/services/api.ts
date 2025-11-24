import { getApiUrl } from '../config';
import type { Library, CreateLibraryDTO, Series, Release } from '../types';

const API_URL = getApiUrl();

// Generic fetch wrapper with error handling
async function fetchApi<T>(
  endpoint: string,
  options?: RequestInit
): Promise<T> {
  const url = `${API_URL}${endpoint}`;

  try {
    const response = await fetch(url, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
    });

    if (!response.ok) {
      throw new Error(`API Error: ${response.status} ${response.statusText}`);
    }

    // Handle empty responses
    const text = await response.text();
    return text ? JSON.parse(text) : ({} as T);
  } catch (error) {
    console.error(`API request failed: ${endpoint}`, error);
    throw error;
  }
}

// Library API
export const libraryApi = {
  // Get all libraries (placeholder - endpoint not yet implemented in backend)
  getAll: async (): Promise<Library[]> => {
    try {
      return await fetchApi<Library[]>('/v1/library/getAllLibraries');
    } catch (error) {
      // Mock data for development until endpoint is implemented
      console.warn('Library getAll endpoint not implemented, using mock data');
      return [];
    }
  },

  // Create a new library
  create: async (library: CreateLibraryDTO): Promise<Library> => {
    return await fetchApi<Library>('/v1/library', {
      method: 'POST',
      body: JSON.stringify(library),
    });
  },

  // Scan a library
  scan: async (libraryId: number): Promise<void> => {
    await fetchApi<void>(`/v1/library/scanLibrary/${libraryId}`, {
      method: 'POST',
    });
  },

  // Delete a library
  delete: async (libraryId: number): Promise<void> => {
    await fetchApi<void>(`/v1/library/${libraryId}`, {
      method: 'DELETE',
    });
  },
};

// Series API
export const seriesApi = {
  // Get all series for a library
  getAllByLibrary: async (
    libraryId: number,
    page = 0,
    pageSize = 100,
    sortBy = 'title'
  ): Promise<Series[]> => {
    try {
      return await fetchApi<Series[]>(
        `/v1/series/getAllSeries?libraryId=${libraryId}&page=${page}&pageSize=${pageSize}&sortBy=${sortBy}`
      );
    } catch (error) {
      // Mock data for development until endpoint is implemented
      console.warn('Series getAllSeries endpoint not implemented, using mock data');
      return [];
    }
  },

  // Refresh metadata for a series
  refreshMetadata: async (seriesId: number): Promise<void> => {
    await fetchApi<void>(`/v1/series/refreshMetadata/${seriesId}`, {
      method: 'POST',
    });
  },
};

// Release API
export const releaseApi = {
  // Get all releases for a series (placeholder - endpoint not yet implemented)
  getAllBySeries: async (seriesId: number): Promise<Release[]> => {
    try {
      // This endpoint doesn't exist yet in the backend
      return await fetchApi<Release[]>(`/v1/release/series/${seriesId}`);
    } catch (error) {
      // Mock data for development until endpoint is implemented
      console.warn('Release getAllBySeries endpoint not implemented, using mock data');
      return [];
    }
  },

  // Download a release
  download: async (releaseId: number): Promise<void> => {
    const url = `${API_URL}/v1/release/download/${releaseId}`;

    try {
      const response = await fetch(url);

      if (!response.ok) {
        throw new Error(`Download failed: ${response.status} ${response.statusText}`);
      }

      // Get the blob from the response
      const blob = await response.blob();

      // Extract filename from Content-Disposition header
      // Backend sends: attachment; filename="sanitized_filename.ext"
      const contentDisposition = response.headers.get('Content-Disposition');
      let filename = `release-${releaseId}`;

      if (contentDisposition) {
        console.log('Content-Disposition:', contentDisposition); // Debug log
        const filenameMatch = contentDisposition.match(/filename="([^"]+)"/);
        if (filenameMatch && filenameMatch[1]) {
          filename = filenameMatch[1];
          console.log('Extracted filename:', filename); // Debug log
        } else {
          console.warn('Failed to extract filename from:', contentDisposition);
        }
      } else {
        console.warn('No Content-Disposition header found');
      }

      // Create a download link and trigger it
      const downloadUrl = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = downloadUrl;
      link.download = filename;
      document.body.appendChild(link);
      link.click();

      // Cleanup
      document.body.removeChild(link);
      window.URL.revokeObjectURL(downloadUrl);
    } catch (error) {
      console.error('Failed to download release:', error);
      throw error;
    }
  },

  // Refresh metadata for a release
  refreshMetadata: async (releaseId: number): Promise<void> => {
    await fetchApi<void>(`/v1/release/refreshMetadata/${releaseId}`, {
      method: 'POST',
    });
  },
};