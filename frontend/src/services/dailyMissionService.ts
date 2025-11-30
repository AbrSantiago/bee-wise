import apiClient from "./api";

export type MissionType =
  | "PLAY_CHALLENGE"
  | "WIN_CHALLENGE"
  | "SPEND_BEECOINS"
  | "EARN_POINTS"
  | "COMPLETE_LESSON"
  | "CORRECT_EXCERSICES"
  | "BYE_ITEMS";

export interface DailyMissionDTO {
  type: MissionType;
  goalAmount: number;
  currentProgress: number;
  rewardAmount: number;
  isClaimed: boolean;
  date: string;     // LocalDate → string ISO
}

export interface DailyMissionUpdateDTO {
  type: MissionType;
  progressAmount: number;
}

export interface DailyMissionUpdateOutDTO {
  type: MissionType;
  goalAmount: number;
  previousProgress: number;
  currentProgress: number;
  rewardAmount: number;
  isClaimed: boolean;
  date: string;
  wasUpdated: boolean;
}

const dailyMissionService = {
  async getAll(): Promise<DailyMissionDTO[]> {
    try {
      const response = await apiClient.get<DailyMissionDTO[]>("/dailyMissions");
      return response.data;
    } catch (error) {
      console.error("❌ Error in dailyMissionService.getAll():", error);
      throw error;
    }
  },

  async updateProgress(
    updates: DailyMissionUpdateDTO[]
  ): Promise<DailyMissionUpdateOutDTO[]> {
    try {
      const response = await apiClient.put<DailyMissionUpdateOutDTO[]>(
        "/dailyMissions/update",
        updates
      );
      return response.data;
    } catch (error) {
      console.error("❌ Error in dailyMissionService.updateProgressMany():", error);
      throw error;
    }
  },
};

export default dailyMissionService;
