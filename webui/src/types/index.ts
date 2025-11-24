// Enums matching backend - using const objects for better compatibility
export const BookType = {
  MANGA: 'MANGA',
  COMIC: 'COMIC',
} as const;
export type BookType = typeof BookType[keyof typeof BookType];

export const ReadDirection = {
  LEFT_TO_RIGHT: 'LEFT_TO_RIGHT',
  RIGHT_TO_LEFT: 'RIGHT_TO_LEFT',
  DEFAULT: 'DEFAULT',
} as const;
export type ReadDirection = typeof ReadDirection[keyof typeof ReadDirection];

export const SeriesType = {
  SERIES: 'SERIES',
  ONE_SHOT: 'ONE_SHOT',
} as const;
export type SeriesType = typeof SeriesType[keyof typeof SeriesType];

export const ReleaseType = {
  VOLUME: 'VOLUME',
  CHAPTER: 'CHAPTER',
  ONE_SHOT: 'ONE_SHOT',
} as const;
export type ReleaseType = typeof ReleaseType[keyof typeof ReleaseType];

// Models matching backend
export interface Library {
  id: number;
  name: string;
  description: string;
  paths: string[];
  readDirection?: ReadDirection;
  libraryType?: BookType;
  seriesType?: SeriesType;
}

export interface CreateLibraryDTO {
  name: string;
  description: string;
  readDirection: ReadDirection;
  libraryType: BookType;
  seriesType: SeriesType;
  paths: string[];
}

export interface Series {
  id: number;
  libraryId: number;
  path: string;
  title: string;
  author?: string;
  description?: string;
  releaseYear?: number;
  metadataSourceId?: string;
}

export interface Release {
  id: number;
  index: number;
  title: string;
  filePath: string;
  releaseDate?: string;
  releaseType: ReleaseType;
  metadataSourceId?: string;
  seriesId: number;
}

// View state types
export type ViewMode = 'series' | 'releases';

export interface AppState {
  selectedLibrary: Library | null;
  selectedSeries: Series | null;
  viewMode: ViewMode;
}