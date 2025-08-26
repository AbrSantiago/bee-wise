import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_URL;

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para requests (debugging y logs)
apiClient.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    console.error('❌ Request Error:', error);
    return Promise.reject(error);
  }
);

// Interceptor para responses (manejo de errores)
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    console.error('❌ Response Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

export default apiClient;