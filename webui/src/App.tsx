import { useState } from 'react';
import { AppProvider } from './context/AppContext';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import MainContent from './components/MainContent';
import CreateLibraryModal from './components/CreateLibraryModal';
import './App.css';

function App() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOpenModal = () => setIsModalOpen(true);
  const handleCloseModal = () => setIsModalOpen(false);
  const handleLibraryCreated = () => {
    // Refresh libraries by triggering a re-render or calling a refresh function
    window.location.reload(); // Simple approach for now
  };

  return (
    <AppProvider>
      <div className="app">
        <Header />
        <div className="app-body">
          <Sidebar onCreateLibrary={handleOpenModal} />
          <MainContent />
        </div>
        <CreateLibraryModal
          isOpen={isModalOpen}
          onClose={handleCloseModal}
          onSuccess={handleLibraryCreated}
        />
      </div>
    </AppProvider>
  );
}

export default App;