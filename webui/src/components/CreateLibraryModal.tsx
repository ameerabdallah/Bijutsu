import React, { useState, useEffect } from 'react';
import { libraryApi } from '../services/api';
import { BookType, ReadDirection, SeriesType } from '../types';
import type { CreateLibraryDTO, Library } from '../types';
import './CreateLibraryModal.css';

interface CreateLibraryModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
  library?: Library; // If provided, edit mode; otherwise, create mode
}

const CreateLibraryModal: React.FC<CreateLibraryModalProps> = ({
  isOpen,
  onClose,
  onSuccess,
  library,
}) => {
  const isEditMode = !!library;
  const [formData, setFormData] = useState<CreateLibraryDTO>({
    name: '',
    description: '',
    readDirection: ReadDirection.DEFAULT,
    libraryType: BookType.MANGA,
    seriesType: SeriesType.SERIES,
    paths: [''],
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Populate form with library data when in edit mode
  useEffect(() => {
    if (isEditMode && library) {
      setFormData({
        name: library.name,
        description: library.description || '',
        readDirection: library.readDirection || ReadDirection.DEFAULT,
        libraryType: library.libraryType || BookType.MANGA,
        seriesType: library.seriesType || SeriesType.SERIES,
        paths: library.paths.length > 0 ? library.paths : [''],
      });
    }
  }, [isEditMode, library]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handlePathChange = (index: number, value: string) => {
    const newPaths = [...formData.paths];
    newPaths[index] = value;
    setFormData((prev) => ({
      ...prev,
      paths: newPaths,
    }));
  };

  const addPath = () => {
    setFormData((prev) => ({
      ...prev,
      paths: [...prev.paths, ''],
    }));
  };

  const removePath = (index: number) => {
    if (formData.paths.length > 1) {
      setFormData((prev) => ({
        ...prev,
        paths: prev.paths.filter((_, i) => i !== index),
      }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Validate
    if (!formData.name.trim()) {
      setError('Library name is required');
      return;
    }

    const validPaths = formData.paths.filter((p) => p.trim());
    if (validPaths.length === 0) {
      setError('At least one path is required');
      return;
    }

    setIsSubmitting(true);

    try {
      if (isEditMode && library) {
        await libraryApi.update(library.id, {
          ...formData,
          paths: validPaths,
        });
      } else {
        await libraryApi.create({
          ...formData,
          paths: validPaths,
        });
      }
      onSuccess();
      resetForm();
      onClose();
    } catch (err) {
      setError(`Failed to ${isEditMode ? 'update' : 'create'} library. Please try again.`);
      console.error(`Error ${isEditMode ? 'updating' : 'creating'} library:`, err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      description: '',
      readDirection: ReadDirection.DEFAULT,
      libraryType: BookType.MANGA,
      seriesType: SeriesType.SERIES,
      paths: [''],
    });
    setError(null);
  };

  const handleClose = () => {
    resetForm();
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{isEditMode ? 'Edit Library' : 'Create New Library'}</h2>
          <button className="modal-close-btn" onClick={handleClose}>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <line x1="18" y1="6" x2="6" y2="18" strokeWidth="2" strokeLinecap="round" />
              <line x1="6" y1="6" x2="18" y2="18" strokeWidth="2" strokeLinecap="round" />
            </svg>
          </button>
        </div>

        <form onSubmit={handleSubmit} className="modal-form">
          {error && <div className="error-message">{error}</div>}

          <div className="form-group">
            <label htmlFor="name">Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              placeholder="My Library"
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Optional description"
              rows={3}
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="libraryType">Library Type</label>
              <select
                id="libraryType"
                name="libraryType"
                value={formData.libraryType}
                onChange={handleChange}
              >
                <option value={BookType.MANGA}>Manga</option>
                <option value={BookType.COMIC}>Comic</option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="seriesType">Series Type</label>
              <select
                id="seriesType"
                name="seriesType"
                value={formData.seriesType}
                onChange={handleChange}
              >
                <option value={SeriesType.SERIES}>Series</option>
                <option value={SeriesType.ONE_SHOT}>One Shot</option>
              </select>
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="readDirection">Read Direction</label>
            <select
              id="readDirection"
              name="readDirection"
              value={formData.readDirection}
              onChange={handleChange}
            >
              <option value={ReadDirection.DEFAULT}>Default</option>
              <option value={ReadDirection.LEFT_TO_RIGHT}>Left to Right</option>
              <option value={ReadDirection.RIGHT_TO_LEFT}>Right to Left</option>
            </select>
          </div>

          <div className="form-group">
            <label>Paths *</label>
            {formData.paths.map((path, index) => (
              <div key={index} className="path-input-group">
                <input
                  type="text"
                  value={path}
                  onChange={(e) => handlePathChange(index, e.target.value)}
                  placeholder="/path/to/library"
                />
                {formData.paths.length > 1 && (
                  <button
                    type="button"
                    className="remove-path-btn"
                    onClick={() => removePath(index)}
                  >
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                      <line x1="18" y1="6" x2="6" y2="18" strokeWidth="2" strokeLinecap="round" />
                      <line x1="6" y1="6" x2="18" y2="18" strokeWidth="2" strokeLinecap="round" />
                    </svg>
                  </button>
                )}
              </div>
            ))}
            <button type="button" className="add-path-btn" onClick={addPath}>
              + Add Path
            </button>
          </div>

          <div className="modal-actions">
            <button type="button" className="btn-secondary" onClick={handleClose}>
              Cancel
            </button>
            <button type="submit" className="btn-primary" disabled={isSubmitting}>
              {isSubmitting
                ? (isEditMode ? 'Updating...' : 'Creating...')
                : (isEditMode ? 'Update Library' : 'Create Library')
              }
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateLibraryModal;