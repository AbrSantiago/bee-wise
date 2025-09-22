import apiClient from "./api";

export type User = {
  id: number;
  username: string;
  name: string;
  surname: string;
  email: string;
};

export type AuthResponse = {
  token: string;
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

  async getCurrentUser(): Promise<User> {
    const response = await apiClient.get<User>("/users/me");
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
};

export default userService;
