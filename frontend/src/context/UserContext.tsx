import { createContext, useContext, useState, useEffect, type ReactNode } from "react";
import { useAuth } from "./AuthContext";
import userService, { type User } from "../services/userService";

export type UserContextType = {
  user: User | null;
  loading: boolean;
  refreshUser: () => Promise<void>;
};

const UserContext = createContext<UserContextType | undefined>(undefined);

export function UserProvider({ children }: { children: ReactNode }) {
  const { token } = useAuth();
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(false);

  const fetchUser = async () => {
    if (!token) {
      setUser(null);
      return;
    }

    setLoading(true);
    try {
      const userData = await userService.getCurrentUser(token); // pasar token acá
      setUser(userData);
      console.log("✅ User loaded:", userData);
    } catch (error) {
      console.error("❌ Error loading user:", error);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const refreshUser = async () => {
    await fetchUser();
  };

  useEffect(() => {
    if (!token) {
      setUser(null);
      return;
    }
    fetchUser();
  }, [token]);

  return (
    <UserContext.Provider value={{ user, loading, refreshUser }}>
      {children}
    </UserContext.Provider>
  );
}

export function useUser() {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser must be used within UserProvider");
  }
  return context;
}