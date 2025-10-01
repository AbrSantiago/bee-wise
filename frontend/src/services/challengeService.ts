import apiClient from "./api";
import type { User } from "./userService";

export type ChallengeStatus = "PENDING" | "ACTIVE" | "EXPIRED" | "COMPLETED";
export type ChallengeResult = "CHALLENGER_WIN" | "CHALLENGED_WIN" | "DRAW" | null;
export type RoundStatus = "COMPLETED" | "WAITING_CHALLENGER" | "WAITING_CHALLENGED";

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
  rol: "CHALLENGER" | "CHALLENGED";
  score: number;
};

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
  
  async getUsersToChallenge(challengerId: number): Promise<User[]> {
    const response = await apiClient.get<User[]>(`/challenge/usersToChallenge/${challengerId}`);
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
};

export default challengeService;
