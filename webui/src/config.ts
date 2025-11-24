// Configuration for API endpoints
// In development, use empty string to leverage Vite proxy
// In production, use full URL or environment variable
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ||
  (import.meta.env.DEV ? '' : 'http://localhost:8080/api');

export const getApiUrl = () => {
  // In the future, this could read from localStorage or a settings file
  // for user-configurable proxy settings
  return API_BASE_URL;
};