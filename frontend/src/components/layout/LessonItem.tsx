// LessonItem.tsx
import { Link } from "react-router-dom";
import "./LessonItem.css";

type LessonItemProps = {
  id: number;
  title: string;
  number: number;
};

export function LessonItem({ id, title, number }: LessonItemProps) {
  return (
    <div className="lesson-node-container">
      <div className="lesson-node">
        <Link to={`/practice/${id}`}>
          <div className="lesson-btn">
            Lesson {number}
          </div>
        </Link>
      </div>
    </div>
  );
}
