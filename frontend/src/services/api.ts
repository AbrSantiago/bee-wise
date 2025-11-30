import axios from "axios";
import userService from "./userService";

const API_BASE_URL = import.meta.env.VITE_API_URL;

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para requests → inyecta el token si existe
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    console.error("❌ Request Error:", error);
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem("refreshToken");
        if (!refreshToken) throw new Error("No refresh token available");

        const newTokens = await userService.refresh(refreshToken);

        localStorage.setItem("accessToken", newTokens.accessToken);
        localStorage.setItem("refreshToken", newTokens.refreshToken);

        // actualizar header y reintentar
        apiClient.defaults.headers.common[
          "Authorization"
        ] = `Bearer ${newTokens.accessToken}`;
        originalRequest.headers[
          "Authorization"
        ] = `Bearer ${newTokens.accessToken}`;

        return apiClient(originalRequest);
      } catch (err) {
        console.error("⚠️ Refresh failed, logging out");
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.location.href = "/landing";
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;