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

const lessonService = {
  async getLesson(id: string): Promise<Lesson> {
    const response = await apiClient.get<Lesson>(`/lesson/${id}`);
    return response.data;
  },
};

export default lessonService;
