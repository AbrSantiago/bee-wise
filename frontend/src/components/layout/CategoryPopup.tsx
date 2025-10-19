import React from 'react';
import './CategoryPopup.css';
import type { ExerciseCategory } from '../../services/challengeService';

interface CategoryPopupProps {
  category: ExerciseCategory;
  onStart: () => void;
}

const CategoryPopup: React.FC<CategoryPopupProps> = ({ category, onStart }) => {
  return (
    <div className="popup-overlay">
      <div className="popup-content">
        <h2>Categoría seleccionada:</h2>
        <h3 className="popup-category-name">{category.replace(/_/g, ' ')}</h3>
        <p>¡Prepárate para responder las preguntas!</p>
        <button onClick={onStart} className="popup-start-btn">
          ¡Comenzar!
        </button>
      </div>
    </div>
  );
};

export default CategoryPopup;