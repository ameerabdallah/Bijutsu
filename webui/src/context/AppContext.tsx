import React, { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';
import type { Library, Series, AppState, ViewMode } from '../types';

interface AppContextType extends AppState {
  setSelectedLibrary: (library: Library | null) => void;
  setSelectedSeries: (series: Series | null) => void;
  setViewMode: (mode: ViewMode) => void;
  isSidebarOpen: boolean;
  toggleSidebar: () => void;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export const AppProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [selectedLibrary, setSelectedLibrary] = useState<Library | null>(null);
  const [selectedSeries, setSelectedSeries] = useState<Series | null>(null);
  const [viewMode, setViewMode] = useState<ViewMode>('series');
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  const handleSetSelectedLibrary = (library: Library | null) => {
    setSelectedLibrary(library);
    setSelectedSeries(null); // Reset series when changing library
    setViewMode('series'); // Reset to series view
  };

  const handleSetSelectedSeries = (series: Series | null) => {
    setSelectedSeries(series);
    if (series) {
      setViewMode('releases');
    }
  };

  return (
    <AppContext.Provider
      value={{
        selectedLibrary,
        selectedSeries,
        viewMode,
        setSelectedLibrary: handleSetSelectedLibrary,
        setSelectedSeries: handleSetSelectedSeries,
        setViewMode,
        isSidebarOpen,
        toggleSidebar,
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

export const useApp = () => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('useApp must be used within AppProvider');
  }
  return context;
};