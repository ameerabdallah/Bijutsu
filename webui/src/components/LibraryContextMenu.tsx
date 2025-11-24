import React, { useEffect, useRef } from 'react';
import type { Library } from '../types';
import './LibraryContextMenu.css';

interface LibraryContextMenuProps {
  library: Library;
  position: { x: number; y: number };
  onScan: (library: Library) => void;
  onClose: () => void;
}

const LibraryContextMenu: React.FC<LibraryContextMenuProps> = ({
  library,
  position,
  onScan,
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
    </div>
  );
};

export default LibraryContextMenu;