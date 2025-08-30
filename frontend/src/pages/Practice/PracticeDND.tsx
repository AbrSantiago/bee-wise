import React, { useState } from 'react';
import { DndContext, type DragEndEvent } from '@dnd-kit/core';
import { Draggable } from '../../components/DragAndDrop/Draggable';
import { Droppable } from '../../components/DragAndDrop/Droppable';
import MainLayout from '../../components/layout/MainLayout';
import './PracticeDND.css';

const matrixA = [
  [2, 3],
  [3, 4],
];

const matrixB = [
  [2, 3],
  [3, 4],
];

const options = [
  { id: 'option1', value: [[2, 3], [3, 4]] },
  { id: 'option2', value: [[4, 6], [6, 8]] }, // correcta
  { id: 'option3', value: [[2, 3], [3, 4]] },
  { id: 'option4', value: [[2, 3], [3, 4]] },
];

function renderMatrix(matrix: number[][], empty = false) {
  return (
    <table className={`matrix${empty ? ' matrix-empty' : ''}`}>
      <tbody>
        {matrix.map((row, i) => (
          <tr key={i}>
            {row.map((cell, j) => (
              <td key={j}>{empty ? '' : cell}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
}

function PracticeDND() {
  const [dropped, setDropped] = useState<string | null>(null);

  function handleDragEnd(event: DragEndEvent) {
    if (event.over && event.over.id === 'result-drop') {
      setDropped(event.active.id);
    }
  }

  return (
    <MainLayout title="Practice Drag and Drop">
      <div className="practice-dnd-container">
        <div className="dnd-row">
          {renderMatrix(matrixA)}
          <span className="dnd-sign">+</span>
          {renderMatrix(matrixB)}
          <span className="dnd-sign">=</span>
          <Droppable id="result-drop">
            {dropped
              ? renderMatrix(options.find(o => o.id === dropped)!.value)
              : renderMatrix([[0, 0], [0, 0]], true)
            }
          </Droppable>
        </div>
        <DndContext onDragEnd={handleDragEnd}>
          <div className="dnd-options">
            {options.map(option =>
              !dropped || dropped !== option.id ? (
                <Draggable key={option.id} id={option.id}>
                  {renderMatrix(option.value)}
                </Draggable>
              ) : null
            )}
          </div>
        </DndContext>
        {dropped && (
          <div className="dnd-feedback">
            {dropped === 'option2'
              ? <span className="dnd-correct">¡Correcto! 🎉</span>
              : <span className="dnd-incorrect">Incorrecto, intenta de nuevo.</span>
            }
          </div>
        )}
      </div>
    </MainLayout>
  );
}

export default PracticeDND;