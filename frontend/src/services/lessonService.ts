import apiClient from "./api";

export type Exercise = {
  id: number;
  question: string;
  answer: string;
  options: null | string[];
  type: "OPEN" | "MULTIPLE_CHOICE";
};

export type Lesson = {
  id: number;
  title: string;
  description?: string;
  exercises: Exercise[];
};

export type LessonCompleteRequest = {
  completedLessonId: number;
  userId: number;
  correctExercises: number;
};

export type LessonCompleteResponse = {
  success: boolean;
  message: string;
  totalPoints: number;
};

const lessonService = {
  async getLesson(id: string): Promise<Lesson> {
    const response = await apiClient.get<Lesson>(`/lesson/${id}`);
    return response.data;
  },

  async lessonComplete(request: LessonCompleteRequest): Promise<LessonCompleteResponse> {
    const response = await apiClient.post<LessonCompleteResponse>("/users/lessonComplete", request);
    return response.data;
  },
};

export default lessonService;
