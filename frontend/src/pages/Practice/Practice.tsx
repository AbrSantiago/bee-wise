import React, { use, useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import 'katex/dist/katex.min.css';
// @ts-ignore
import { BlockMath } from 'react-katex';
import axios from "axios";
import './Practice.css';


export function PracticePage() {

  const [exercise, setExercise] = useState({
  question: '',
  matrixA: [] as number[][],
  matrixB: [] as number[][]
});

  const fetchPractice = async () => {
    return axios.get('http://localhost:8080/exercise/1')
    .then(response => {

      const extractedMatrices = extractMatricesFromLatex(response.data.question);
      console.log('Matrices extraídas:', extractedMatrices);
      setExercise({
        question: response.data.question,
        matrixA: extractedMatrices[0] || [],
        matrixB: extractedMatrices[1] || []
});
      console.log(response.data);
      
      
    
    })
    .catch(error => {
      console.error('There was an error fetching the practice data!', error);
    });
  };

  useEffect(() => {
    fetchPractice();
  }, []);

  const extractMatricesFromLatex = (latexString) => {
  console.log('🔍 String original:', latexString);
  
  const matrices = [];
  const matrixRegex = /\\begin\{bmatrix\}(.*?)\\end\{bmatrix\}/g;
  
  let match;
  while ((match = matrixRegex.exec(latexString)) !== null) {
    const matrixContent = match[1].trim();
    console.log('📦 Contenido de matriz encontrado:', `"${matrixContent}"`);
    
    // El separador correcto es \\ (dos backslashes)
    const rows = matrixContent.split(' \\\\ ');
    console.log('📋 Filas después del split:', rows);
    
    // Procesar cada fila
    const matrix = rows.map((row, index) => {
      console.log(`🔢 Procesando fila ${index}:`, `"${row}"`);
      const cells = row.trim().split(' & ');
      console.log(`📊 Celdas en fila ${index}:`, cells);
      return cells.map(cell => parseInt(cell.trim()));
    });
    
    console.log('✅ Matriz procesada:', matrix);
    matrices.push(matrix);
  }
  
  return matrices;
};

const A = exercise.matrixA;

const B = exercise.matrixB;

  const result = A.map((row, i) => row.map((v, j) => v + B[i][j]));

  const [userMatrix, setUserMatrix] = useState<string[][]>([]);

  const [feedback, setFeedback] = useState<null | boolean>(null);

  useEffect(() => {
  if (A.length > 0 && A[0] && A[0].length > 0) {
    setUserMatrix(
      Array(A.length).fill(null).map(() => Array(A[0].length).fill(""))
    );
  }
}, [A]);

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
      {A.length > 0 && B.length > 0 ? (
        <>
          {/* Operación A + B */}
          <BlockMath
            math={`\\begin{bmatrix} 
              ${A.map(row => row.join(" & ")).join(" \\\\ ")} 
            \\end{bmatrix} + 
            \\begin{bmatrix} 
              ${B.map(row => row.join(" & ")).join(" \\\\ ")} 
            \\end{bmatrix} =`}
          />

          {/* Instrucción para el usuario */}
          <p className="text-lg font-medium text-gray-700 mb-4">Put the result here:</p>

          {/* Matriz editable */}
          <div className="matrix-container">
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
          </div>

          {/* Botón */}
          <button
            onClick={checkAnswer}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            Check
          </button>

          {/* Feedback */}
          {feedback !== null && (
            feedback ? (
              <p className="text-green-600 font-bold">✅ Excellent!</p>
            ) : (
              <p className="text-red-600 font-bold">❌ Try again</p>
            )
          )}
        </>
      ) : (
        <p className="text-gray-500">Cargando ejercicio...</p>
      )}
    </div>
  </MainLayout>
);
}