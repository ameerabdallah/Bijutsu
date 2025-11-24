# Bijutsu WebUI

A React-based web interface for the Bijutsu manga/comic library management system.

## Features

- **Library Management**: View, create, and scan libraries
- **Series Browsing**: Browse series within each library
- **Release Management**: View and download releases for each series
- **Responsive UI**: Dark-themed interface with sidebar navigation
- **Configurable API**: Support for proxying to backend server

## Getting Started

### Prerequisites

- Node.js 16+ and npm

### Installation

1. Install dependencies:
```bash
npm install
```

2. Configure the API endpoint (optional):
```bash
cp .env.example .env
# Edit .env to set VITE_API_BASE_URL if your API is not at http://localhost:8080
```

### Development

Run the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:5173`

### Building for Production

Build the application:
```bash
npm run build
```

The built files will be in the `dist` directory.

Preview the production build:
```bash
npm run preview
```

## Configuration

The WebUI can be configured using environment variables:

- `VITE_API_BASE_URL`: The base URL of the Bijutsu API server (default: `http://localhost:8080`)

Create a `.env` file in the root of the `webui` directory to override these values.

## Project Structure

```
webui/
├── src/
│   ├── components/          # React components
│   │   ├── Header.tsx       # Top navigation with hamburger menu
│   │   ├── Sidebar.tsx      # Library list sidebar
│   │   ├── MainContent.tsx  # Main view (series/releases table)
│   │   ├── CreateLibraryModal.tsx
│   │   └── LibraryContextMenu.tsx
│   ├── context/             # React context for state management
│   │   └── AppContext.tsx
│   ├── services/            # API service layer
│   │   └── api.ts
│   ├── types/               # TypeScript type definitions
│   │   └── index.ts
│   ├── config.ts            # Configuration
│   ├── App.tsx              # Main application component
│   └── main.tsx             # Application entry point
├── .env.example             # Example environment variables
└── README.md                # This file
```

## Usage

1. **Open the Sidebar**: Click the hamburger icon in the top-left to open the library sidebar
2. **Create a Library**: Click the + button in the sidebar to create a new library
3. **Select a Library**: Click on a library in the sidebar to view its series
4. **Scan a Library**: Click the ⋮ (more) button next to a library and select "Scan Library"
5. **View Series**: Series are displayed in a table in the main content area
6. **View Releases**: Click on a series to view its releases
7. **Download Release**: Click on a release to download it

## API Integration

The WebUI integrates with the following backend endpoints:

- `GET /v1/library` - Get all libraries
- `POST /v1/library` - Create a new library
- `POST /v1/library/scanLibrary/{id}` - Scan a library
- `GET /v1/series/getAllSeries` - Get series for a library
- `GET /v1/release/series/{id}` - Get releases for a series (placeholder)
- `POST /v1/release/download/{id}` - Download a release (placeholder)

Note: Some endpoints are not yet fully implemented in the backend and will return mock data or errors.

## Technologies Used

- **React 18**: UI library
- **TypeScript**: Type-safe JavaScript
- **Vite**: Fast build tool and dev server
- **Context API**: State management
- **CSS**: Styling (no external CSS frameworks for simplicity)
