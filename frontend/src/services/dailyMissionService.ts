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

const dailyMissionService = {
  async getAll(): Promise<DailyMissionDTO[]> {
    console.log("🔄 Calling GET /daily-mission");
    try {
      const response = await apiClient.get<DailyMissionDTO[]>("/dailyMissions");
      console.log("📨 Response from /daily-mission:", response);
      console.log("📦 Response data:", response.data);
      return response.data;
    } catch (error) {
      console.error("❌ Error in dailyMissionService.getAll():", error);
      throw error;
    }
  },
};

export default dailyMissionService;
