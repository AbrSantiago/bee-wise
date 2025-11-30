import { LessonItem } from "./LessonItem";
import "./LessonPath.css";

type Lesson = {
  id: number;
  title: string;
  description: string;
};

type LessonPathProps = {
  lessons: Lesson[];
};

export function LessonPath({ lessons }: LessonPathProps) {
  return (
    <div className="lesson-path-container">
      <div className="lesson-path">
        {lessons.map((lesson, index) => (
          <LessonItem
            key={lesson.id}
            id={lesson.id}
            title={lesson.title}
            number={index + 1}
          />
        ))}
      </div>
    </div>
  );
}
