import apiClient from "./api";

export type User = {
  id: number;
  name: string;
  surname: string;
  email: string;
  username: string;
  points: number;
};

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
  }
};

export default userService;
