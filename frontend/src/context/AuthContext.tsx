// src/context/AuthContext.tsx
import { createContext, useContext, useState, type ReactNode } from "react";
import { useNavigate } from "react-router-dom";
import apiClient from "../services/api";
import type { AuthResponse } from "../services/userService";

type AuthContextType = {
  accessToken: string | null;
  refreshToken: string | null;
  login: (tokens: AuthResponse) => void;
  logout: () => void;
  refresh: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [accessToken, setAccessToken] = useState<string | null>(
    localStorage.getItem("accessToken")
  );
  const [refreshToken, setRefreshToken] = useState<string | null>(
    localStorage.getItem("refreshToken")
  );
  const navigate = useNavigate();

  const login = ({ accessToken, refreshToken }: AuthResponse) => {
    setAccessToken(accessToken);
    setRefreshToken(refreshToken);
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
    navigate("/");
  };

  const logout = () => {
    setAccessToken(null);
    setRefreshToken(null);
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate("/landing");
  };

  const refresh = async () => {
    if (!refreshToken) {
      logout();
      return;
    }
    try {
      const res = await apiClient.post<AuthResponse>("/users/auth/refresh", {
        refreshToken,
      });
      setAccessToken(res.data.accessToken);
      setRefreshToken(res.data.refreshToken);
      localStorage.setItem("accessToken", res.data.accessToken);
      localStorage.setItem("refreshToken", res.data.refreshToken);
    } catch (e) {
      console.error("⚠️ Error refreshing token", e);
      logout();
    }
  };

  return (
    <AuthContext.Provider
      value={{ accessToken, refreshToken, login, logout, refresh }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
