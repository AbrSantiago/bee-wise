import React from 'react';
import { useEffect, useState } from "react";


export function ButtonCheck() {
  
  const [userMatrix, setUserMatrix] = React.useState<number[][]>([]);
  const [result, setResult] = React.useState<number[][]>([]);
  const [feedback, setFeedback] = React.useState<boolean | null>(null);

  const A = exercise.matrixA;

  const B = exercise.matrixB;

  const checkAnswer = () => {
    const correct = userMatrix.every((row, i) =>
      row.every((val, j) => Number(val) === result[i][j])
    );
    setFeedback(correct);
  };

  useEffect(() => {
    if (A.length > 0 && A[0] && A[0].length > 0) {
      setUserMatrix(
        Array(A.length).fill(null).map(() => Array(A[0].length).fill(""))
      );
    }
  }, [A]);

  return (
    <button
      onClick={checkAnswer}
      className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
    >
      Check
    </button>
  );
}
