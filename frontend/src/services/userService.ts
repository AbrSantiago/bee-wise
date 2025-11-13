import apiClient from "./api";

export type User = {
  id: number;
  name: string;
  surname: string;
  email: string;
  username: string;
  points: number;
  avatar: Avatar;
  items: ShopItem[];
  beeCoins: number;
  ranking: number;
};

export type Avatar = {
  id: number;
  skin: ShopItem;
  hair: ShopItem;
  shirt: ShopItem;
  background: ShopItem;
}

export type ShopItem = {
  id: number;
  name: string;
  category: ItemCategory;
  image: string;
  price: number;
}

export type ItemCategory = "SHIRT" | "SKIN" | "HAIR" | "BACKGROUND"

export type AuthResponse = {
  accessToken: string;
  refreshToken: string;
};

export type UserPointsResponse = {
  userId: number;
  username: string;
  points: number;
  currentLesson: number;
};

export interface UserStatsDTO {
  challengesPlayed: number;
  challengesWon: number;
  winRate: number;
  roundsWon: number;
  avgCorrectAnswersPerChallenge: number;

  totalPoints: number;
  beeCoinsSpent: number;
  longestWinStreak: number;
  itemsObtained: number;

  percentile: number; // e.g., 85 means "better than 85% of players"
  accuracy: number;   // e.g., 0.8 means 80%
}

export interface UserToChallengeDTO {
  id: number;
  username: string;
  avatar: Avatar;
  points: number;
}

const userService = {
  async login(username: string, password: string): Promise<AuthResponse> {
    const response = await apiClient.post<AuthResponse>(
      "/users/auth/login",
      { username, password }
    );
    return response.data;
  },

  async register(data: {
    name: string;
    surname: string;
    email: string;
    username: string;
    password: string;
  }): Promise<User> {
    const response = await apiClient.post<User>(
      "/users/auth/register",
      data
    );
    return response.data;
  },

  async getCurrentUser(token?: string): Promise<User> {
    const response = await apiClient.get<User>("/users/me", {
      headers: token ? { Authorization: `Bearer ${token}` } : undefined,
    });
    return response.data;
  },

  async logout(): Promise<void> {
    try {
      await apiClient.post("/users/auth/logout");
    } catch (err) {
      console.warn("⚠️ Error calling logout endpoint, cleaning local anyway");
    }
    localStorage.removeItem("token");
  },

  async getUserPoints(): Promise<UserPointsResponse> {
    const response = await apiClient.get<UserPointsResponse>("/users/points");
    return response.data;
  },

  async getUsers(): Promise<User[]> {
    const response = await apiClient.get<User[]>("/users");
    return response.data;
  },

  async refresh(refreshToken: string): Promise<AuthResponse> {
    const response = await apiClient.post<AuthResponse>(
      "/users/auth/refresh",
      { refreshToken }
    );
    return response.data;
  },

  async getUserItems(token: string): Promise<ShopItem[]> {
    const response = await apiClient.get<ShopItem[]>("/users/items", {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  },

  async updateAvatar(userId: number, avatar: Avatar): Promise<User> {
    const response = await apiClient.put<User>(`/users/updateAvatar/${userId}`, avatar);
    return response.data;
  },

  async getUserItemsByCategory(token: string): Promise<Record<ItemCategory, ShopItem[]>> {
    const response = await apiClient.get<Record<ItemCategory, ShopItem[]>>("/users/items", {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  },

  async getUserStats(token: string): Promise<UserStatsDTO> {
    try {
      const response = await apiClient.get<UserStatsDTO>("/users/stats", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching user stats:", error);
      throw error;
    }
  },
};

export default userService;
