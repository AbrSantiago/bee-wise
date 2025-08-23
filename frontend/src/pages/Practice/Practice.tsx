import React, { useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import 'katex/dist/katex.min.css';
// @ts-ignore
import { BlockMath } from 'react-katex';


export function PracticePage() {
const A = [
    [1, 2, 3],
    [4, 5, 6],
  ];

  const B = [
    [1, 2, 3],
    [4, 5, 6],
  ];

  const result = A.map((row, i) => row.map((v, j) => v + B[i][j]));

  const [userMatrix, setUserMatrix] = useState(
    Array(A.length).fill(null).map(() => Array(A[0].length).fill(""))
  );
  const [feedback, setFeedback] = useState<null | boolean>(null);

  const handleChange = (i: number, j: number, value: string) => {
    const newMatrix = userMatrix.map((row, ri) =>
      row.map((col, cj) => (ri === i && cj === j ? value : col))
    );
    setUserMatrix(newMatrix);
    setFeedback(null);
  };

  const checkAnswer = () => {
    const correct = userMatrix.every((row, i) =>
      row.every((val, j) => Number(val) === result[i][j])
    );
    setFeedback(correct);
  };

  return (
    <MainLayout title="Practice Page">
      <h1>Practice Page</h1>
      <div className="flex flex-col items-center space-y-6">
        {/* Operación A + B */}
        <BlockMath
          math={`\\begin{bmatrix} 
            ${A[0].join(" & ")} \\\\ 
            ${A[1].join(" & ")} 
          \\end{bmatrix} + 
          \\begin{bmatrix} 
            ${B[0].join(" & ")} \\\\ 
            ${B[1].join(" & ")} 
          \\end{bmatrix} =`}
        />

        {/* Matriz editable */}
        <div className="flex items-center">
          <span className="text-4xl">[</span>
          <table className="m-2">
            <tbody>
              {userMatrix.map((row, i) => (
                <tr key={i}>
                  {row.map((val, j) => (
                    <td key={j} className="px-2">
                      <input
                        type="text"
                        value={val}
                        onChange={(e) => handleChange(i, j, e.target.value)}
                        className="w-12 text-center border rounded"
                      />
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
          <span className="text-4xl">]</span>
        </div>

        {/* Botón */}
        <button
          onClick={checkAnswer}
          className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          Verificar
        </button>

        {/* Feedback */}
        {feedback !== null && (
          feedback ? (
            <p className="text-green-600 font-bold">✅ Correcto!</p>
          ) : (
            <p className="text-red-600 font-bold">❌ Intenta de nuevo</p>
          )
        )}
      </div>
    </MainLayout>
  );
}