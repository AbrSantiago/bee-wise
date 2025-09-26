import {
  createContext,
  useContext,
  useState,
  useEffect,
  type ReactNode,
} from "react";
import { useAuth } from "./AuthContext";
import userService, { type UserPointsResponse } from "../services/userService";

type UserPointsContextType = {
  userPoints: UserPointsResponse | null;
  loading: boolean;
  refreshPoints: () => Promise<void>;
};

const UserPointsContext = createContext<UserPointsContextType | undefined>(
  undefined
);

export function UserPointsProvider({ children }: { children: ReactNode }) {
  const { token } = useAuth();
  const [userPoints, setUserPoints] = useState<UserPointsResponse | null>(null);
  const [loading, setLoading] = useState(false);

  const fetchPoints = async () => {
    if (!token) {
      console.log("⚠️ No token available");
      return;
    }

    setLoading(true);
    try {
      const data = await userService.getUserPoints();
      setUserPoints(data);
    } catch (error) {
      console.error("❌ Error fetching user points:", error);
    } finally {
      setLoading(false);
    }
  };

  const refreshPoints = async () => {
    await fetchPoints();
  };

  useEffect(() => {
    if (token) {
      fetchPoints();
    } else {
      setUserPoints(null);
    }
  }, [token]);

  return (
    <UserPointsContext.Provider value={{ userPoints, loading, refreshPoints }}>
      {children}
    </UserPointsContext.Provider>
  );
}

export function useUserPoints() {
  const context = useContext(UserPointsContext);
  if (!context) {
    throw new Error("useUserPoints must be used within UserPointsProvider");
  }
  return context;
}
