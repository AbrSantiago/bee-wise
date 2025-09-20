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
      <Link to={`/practice/${id}`}>
        <div className="lesson-node">
          <div className="hive"></div>
          <button className="lesson-btn">{`Lección ${number}`}</button>
        </div>
      </Link>
    </div>
  );
}
