import apiClient from "./api";
import type { Exercise } from "./lessonService";
import type { Avatar, ShopItem, User, UserToChallengeDTO } from "./userService";

export type ChallengeStatus = "PENDING" | "ACTIVE" | "EXPIRED" | "COMPLETED";
export type ChallengeResult = "CHALLENGER_WIN" | "CHALLENGED_WIN" | "DRAW" | null;
export type RoundStatus = "COMPLETED" | "WAITING_CHALLENGER" | "WAITING_CHALLENGED";
export type ExerciseCategory =
  | "MATRICES"
  | "DETERMINANTS"
  | "SYSTEM_OF_EQUATIONS"
  | "GROUP_THEORY"
  | "VECTOR_SPACES"
  | "DIVISIBILITY";

export const ExerciseCategoryNames: Record<ExerciseCategory, string> = {
  MATRICES: "Matrices",
  DETERMINANTS: "Determinantes",
  SYSTEM_OF_EQUATIONS: "Sistemas de ecuaciones",
  GROUP_THEORY: "Teoría de grupos",
  VECTOR_SPACES: "Espacios vectoriales",
  DIVISIBILITY: "Divisibilidad",
};

export type RoundDTO = {
  roundNumber: number;
  challengerScore: number;
  challengedScore: number;
  status: RoundStatus;
};

export type ChallengeDTO = {
  id: number;
  challengerId: number;
  challengedId: number;
  status: ChallengeStatus;
  rounds: RoundDTO[];
  maxRounds: number;
  questionsPerRound: number;
  creationDate: string;
  expireDate: string;
  result: ChallengeResult;
};

export type SendChallengeDTO = {
  challengerId: number;
  challengedId: number;
  maxRounds: number;
  questionsPerRound: number;
};

export type AnswerDTO = {
  challengeId: number;
  roundNumber: number;
  rol: ChallengeRol;
  score: number;
  correctAnswers: number;
};

export type ChallengeSummaryDTO = {
  username: string;
  avatar: Avatar;
  opponentUsername: string;
  opponentAvatar: Avatar;
  roundsWon: number;
  totalRounds: number;
  beeCoins: number;
  points: number;
  item: ShopItem;
  winner: ChallengeWinner;
};

export type ChallengeWinner = "ME" | "OPPONENT" | "DRAW";

export type ChallengeRol = "CHALLENGER" | "CHALLENGED";

const challengeService = {
  async getAll(): Promise<ChallengeDTO[]> {
    console.log("🔄 Calling GET /challenge");
    try {
      const response = await apiClient.get<ChallengeDTO[]>("/challenge");
      console.log("📨 Response from /challenge:", response);
      console.log("📦 Response data:", response.data);
      return response.data;
    } catch (error) {
      console.error("❌ Error in challengeService.getAll():", error);
      throw error;
    }
  },
  
  async getUsersToChallenge(challengerId: number): Promise<UserToChallengeDTO[]> {
    const response = await apiClient.get<UserToChallengeDTO[]>(`/challenge/usersToChallenge/${challengerId}`);
    return response.data;
  },

  async sendChallenge(data: SendChallengeDTO): Promise<ChallengeDTO> {
    const response = await apiClient.post<ChallengeDTO>(
      "/challenge/send",
      data
    );
    return response.data;
  },

  async acceptChallenge(challengeId: number): Promise<ChallengeDTO> {
    const response = await apiClient.post<ChallengeDTO>(
      `/challenge/accept/${challengeId}`
    );
    return response.data;
  },

  async answerRound(data: AnswerDTO): Promise<ChallengeDTO> {
    const response = await apiClient.post<ChallengeDTO>(
      "/challenge/answer",
      data
    );
    return response.data;
  },

  async getRandomExercises(limit: number, category: ExerciseCategory): Promise<Exercise[]> {
    try {
      const response = await apiClient.get<Exercise[]>(
        `/challenge/randomExercises?limit=${limit}&category=${category}`
      );
      return response.data;
    } catch (error) {
      console.error("❌ Error fetching random exercises:", error);
      throw error;
    }
  },

  async getRandomCategory(): Promise<ExerciseCategory> {
    try {
      const response = await apiClient.get<ExerciseCategory>(
        "/challenge/getRandomCategory"
      );
      return response.data;
    } catch (error) {
      console.error("❌ Error fetching random category:", error);
      throw error;
    }
  },

  async getAllCategories(): Promise<ExerciseCategory[]> {
    try {
      const response = await apiClient.get<ExerciseCategory[]>(
        "/challenge/categories"
      );
      return response.data;
    } catch (error) {
      console.error("❌ Error fetching all categories:", error);
      throw error;
    }
  },

  async getChallengeOpponent(challengeId: number, username: string): Promise<User> {
    try {
      const response = await apiClient.get<User>(
        `/challenge/${challengeId}/opponent/${username}`
      );
      return response.data;
    } catch (error) {
      console.error("❌ Error fetching challenge opponent:", error);
      throw error;
    }
  },

  async getChallengeSummary(challengeId: number, token: string): Promise<ChallengeSummaryDTO> {
    try {
      const response = await apiClient.get<ChallengeSummaryDTO>(
        `/challenge/${challengeId}/summary`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("❌ Error fetching challenge summary:", error);
      throw error;
    }
  },
};

export default challengeService;