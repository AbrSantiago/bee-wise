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
          <button className="lesson-btn">
            <span className="star">★</span>
          </button>
        </Link>
        <div className="lesson-text-container">
          <span className="lesson-btn-label">{`Lección ${number}:`}</span>
          <span className="lesson-btn-label">{`${title}`}</span>
        </div>
      </div>
    </div>
  );
}
