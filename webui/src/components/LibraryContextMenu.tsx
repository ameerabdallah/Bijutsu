import React, { useEffect, useRef } from 'react';
import type { Library } from '../types';
import './LibraryContextMenu.css';

interface LibraryContextMenuProps {
  library: Library;
  position: { x: number; y: number };
  onScan: (library: Library) => void;
  onEdit: (library: Library) => void;
  onDelete: (library: Library) => void;
  onClose: () => void;
}

const LibraryContextMenu: React.FC<LibraryContextMenuProps> = ({
  library,
  position,
  onScan,
  onEdit,
  onDelete,
  onClose,
}) => {
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        onClose();
      }
    };

    const handleEscape = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        onClose();
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    document.addEventListener('keydown', handleEscape);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      document.removeEventListener('keydown', handleEscape);
    };
  }, [onClose]);

  return (
    <div
      ref={menuRef}
      className="context-menu"
      style={{
        left: `${position.x}px`,
        top: `${position.y}px`,
      }}
    >
      <button className="context-menu-item" onClick={() => onScan(library)}>
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <path d="M3 3h7v7H3zM14 3h7v7h-7zM14 14h7v7h-7zM3 14h7v7H3z" strokeWidth="2" />
        </svg>
        Scan Library
      </button>
      <button className="context-menu-item" onClick={() => onEdit(library)}>
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
          <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
        Edit Library
      </button>
      <button className="context-menu-item" onClick={() => onDelete(library)}>
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <path d="M3 6h18M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
        Delete Library
      </button>
    </div>
  );
};

export default LibraryContextMenu;