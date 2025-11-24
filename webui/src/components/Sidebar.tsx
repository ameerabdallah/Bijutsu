import React, { useEffect, useState } from 'react';
import { useApp } from '../context/AppContext';
import { libraryApi } from '../services/api';
import type { Library } from '../types';
import LibraryContextMenu from './LibraryContextMenu';
import './Sidebar.css';

interface SidebarProps {
  onCreateLibrary: () => void;
  onEditLibrary: (library: Library) => void;
}

const Sidebar: React.FC<SidebarProps> = ({ onCreateLibrary, onEditLibrary }) => {
  const { isSidebarOpen, toggleSidebar, selectedLibrary, setSelectedLibrary } = useApp();
  const [libraries, setLibraries] = useState<Library[]>([]);
  const [loading, setLoading] = useState(false);
  const [contextMenu, setContextMenu] = useState<{
    library: Library;
    x: number;
    y: number;
  } | null>(null);

  useEffect(() => {
    loadLibraries();
  }, []);

  const loadLibraries = async () => {
    setLoading(true);
    try {
      const data = await libraryApi.getAll();
      setLibraries(data);
    } catch (error) {
      console.error('Failed to load libraries:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLibraryClick = (library: Library) => {
    setSelectedLibrary(library);
  };

  const handleMoreClick = (e: React.MouseEvent, library: Library) => {
    e.stopPropagation();
    setContextMenu({
      library,
      x: e.clientX,
      y: e.clientY,
    });
  };

  const handleScanLibrary = async (library: Library) => {
    try {
      await libraryApi.scan(library.id);
      alert(`Scanning library: ${library.name}`);
    } catch (error) {
      console.error('Failed to scan library:', error);
      alert('Failed to scan library');
    }
    setContextMenu(null);
  };

  const handleEditLibrary = (library: Library) => {
    onEditLibrary(library);
    setContextMenu(null);
  };

  const handleDeleteLibrary = async (library: Library) => {
    if (!window.confirm(`Are you sure you want to delete "${library.name}"? This action cannot be undone.`)) {
      setContextMenu(null);
      return;
    }

    try {
      await libraryApi.delete(library.id);
      alert(`Library "${library.name}" has been deleted successfully.`);

      // Clear selection if the deleted library was selected
      if (selectedLibrary?.id === library.id) {
        setSelectedLibrary(null);
      }

      // Reload the library list
      await loadLibraries();
    } catch (error) {
      console.error('Failed to delete library:', error);
      alert(`Failed to delete library "${library.name}". Please try again.`);
    }

    setContextMenu(null);
  };

  const handleCloseContextMenu = () => {
    setContextMenu(null);
  };

  return (
    <>
      <aside className={`sidebar ${isSidebarOpen ? 'open' : ''}`}>
        <div className="sidebar-content">
          <div className="sidebar-header">
            <h2>Libraries</h2>
            <button className="add-library-btn" onClick={onCreateLibrary} title="Create new library">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                <line x1="12" y1="5" x2="12" y2="19" strokeWidth="2" strokeLinecap="round" />
                <line x1="5" y1="12" x2="19" y2="12" strokeWidth="2" strokeLinecap="round" />
              </svg>
            </button>
          </div>

          <div className="library-list">
          {loading ? (
            <div className="loading">Loading libraries...</div>
          ) : libraries.length === 0 ? (
            <div className="empty-state">
              <p>No libraries found</p>
              <p className="empty-state-hint">Click + to create one</p>
            </div>
          ) : (
            libraries.map((library) => (
              <div
                key={library.id}
                className={`library-item ${selectedLibrary?.id === library.id ? 'active' : ''}`}
                onClick={() => handleLibraryClick(library)}
              >
                <div className="library-info">
                  <div className="library-name">{library.name}</div>
                  {library.description && (
                    <div className="library-description">{library.description}</div>
                  )}
                </div>
                <button
                  className="library-more-btn"
                  onClick={(e) => handleMoreClick(e, library)}
                  aria-label="More options"
                >
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                    <circle cx="12" cy="5" r="2" />
                    <circle cx="12" cy="12" r="2" />
                    <circle cx="12" cy="19" r="2" />
                  </svg>
                </button>
              </div>
            ))
          )}
          </div>
        </div>
      </aside>

      {contextMenu && (
        <LibraryContextMenu
          library={contextMenu.library}
          position={{ x: contextMenu.x, y: contextMenu.y }}
          onScan={handleScanLibrary}
          onEdit={handleEditLibrary}
          onDelete={handleDeleteLibrary}
          onClose={handleCloseContextMenu}
        />
      )}
    </>
  );
};

export default Sidebar;