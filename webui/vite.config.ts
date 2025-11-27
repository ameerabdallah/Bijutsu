import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: "0.0.0.0",
    allowedHosts: ["bijutsu.ameerdev.com"],
    proxy: {
      '/v1': {
        target: 'http://localhost:8080/api',
        changeOrigin: true,
        secure: false
      },
    },
  },
})
