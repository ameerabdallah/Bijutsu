import React from 'react';
import { useApp } from '../context/AppContext';
import './Header.css';

const Header: React.FC = () => {
  const { toggleSidebar, selectedLibrary } = useApp();

  return (
    <header className="header">
      <button className="hamburger-btn" onClick={toggleSidebar} aria-label="Toggle sidebar">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <line x1="3" y1="6" x2="21" y2="6" strokeWidth="2" strokeLinecap="round" />
          <line x1="3" y1="12" x2="21" y2="12" strokeWidth="2" strokeLinecap="round" />
          <line x1="3" y1="18" x2="21" y2="18" strokeWidth="2" strokeLinecap="round" />
        </svg>
      </button>
      <h1 className="header-title">
        Bijutsu {selectedLibrary && `- ${selectedLibrary.name}`}
      </h1>
    </header>
  );
};

export default Header;