-- Ejercicios de tipo OPEN (Verdadero/Falso) sobre teoría de álgebra lineal
INSERT INTO exercise (question, answer, type) VALUES 
('¿Es verdad que la multiplicación de matrices es conmutativa?', 'Falso', 'OpenExercise');

INSERT INTO exercise (question, answer, type) VALUES 
('¿Es cierto que el determinante de una matriz triangular superior es igual al producto de sus elementos diagonales?', 'Verdadero', 'OpenExercise');

INSERT INTO exercise (question, answer, type) VALUES 
('¿Es verdad que si una matriz tiene una fila de ceros, entonces su determinante es cero?', 'Verdadero', 'OpenExercise');

INSERT INTO exercise (question, answer, type) VALUES 
('¿Es cierto que la suma de matrices es asociativa?', 'Verdadero', 'OpenExercise');

INSERT INTO exercise (question, answer, type) VALUES 
('¿Es verdad que toda matriz cuadrada tiene inversa?', 'Falso', 'OpenExercise');

INSERT INTO exercise (question, answer, type) VALUES 
('¿Es cierto que el rango de una matriz es siempre menor o igual al mínimo entre el número de filas y columnas?', 'Verdadero', 'OpenExercise');

-- Ejercicios de tipo MULTIPLE_CHOICE

-- 1. Multiplicación de matriz por escalar
INSERT INTO exercise (question, answer, type) VALUES 
('Calcule: 3 \cdot \begin{bmatrix} 2 & 1 \\ 4 & 3 \end{bmatrix} = ?', '\begin{bmatrix} 6 & 3 \\ 12 & 9 \end{bmatrix}', 'MultipleChoiceExercise');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE 'Calcule: 3 \cdot \begin{bmatrix} 2 & 1%'), '\begin{bmatrix} 6 & 3 \\ 12 & 9 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE 'Calcule: 3 \cdot \begin{bmatrix} 2 & 1%'), '\begin{bmatrix} 5 & 4 \\ 7 & 6 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE 'Calcule: 3 \cdot \begin{bmatrix} 2 & 1%'), '\begin{bmatrix} 6 & 4 \\ 12 & 8 \end{bmatrix}');

-- 2. Resta de matrices
INSERT INTO exercise (question, answer, type) VALUES 
('\begin{bmatrix} 8 & 5 \\ 6 & 9 \end{bmatrix} - \begin{bmatrix} 3 & 2 \\ 1 & 4 \end{bmatrix} = ?', '\begin{bmatrix} 5 & 3 \\ 5 & 5 \end{bmatrix}', 'MultipleChoiceExercise');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 8 & 5%'), '\begin{bmatrix} 5 & 3 \\ 5 & 5 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 8 & 5%'), '\begin{bmatrix} 5 & 3 \\ 7 & 5 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 8 & 5%'), '\begin{bmatrix} 11 & 7 \\ 7 & 13 \end{bmatrix}');

-- 3. Multiplicación de matrices
INSERT INTO exercise (question, answer, type) VALUES 
('\begin{bmatrix} 1 & 2 \\ 3 & 4 \end{bmatrix} \cdot \begin{bmatrix} 2 & 0 \\ 1 & 3 \end{bmatrix} = ?', '\begin{bmatrix} 4 & 6 \\ 10 & 12 \end{bmatrix}', 'MultipleChoiceExercise');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 1 & 2 \\ 3 & 4 \end{bmatrix} \cdot%'), '\begin{bmatrix} 4 & 6 \\ 10 & 12 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 1 & 2 \\ 3 & 4 \end{bmatrix} \cdot%'), '\begin{bmatrix} 2 & 6 \\ 7 & 12 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 1 & 2 \\ 3 & 4 \end{bmatrix} \cdot%'), '\begin{bmatrix} 4 & 5 \\ 10 & 11 \end{bmatrix}');

-- 4. Otro ejercicio de multiplicación por escalar
INSERT INTO exercise (question, answer, type) VALUES 
('Calcule: -2 \cdot \begin{bmatrix} 3 & -1 \\ 0 & 2 \end{bmatrix} = ?', '\begin{bmatrix} -6 & 2 \\ 0 & -4 \end{bmatrix}', 'MultipleChoiceExercise');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE 'Calcule: -2 \cdot \begin{bmatrix} 3 & -1%'), '\begin{bmatrix} -6 & 2 \\ 0 & -4 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE 'Calcule: -2 \cdot \begin{bmatrix} 3 & -1%'), '\begin{bmatrix} 6 & -2 \\ 0 & 4 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE 'Calcule: -2 \cdot \begin{bmatrix} 3 & -1%'), '\begin{bmatrix} -6 & -2 \\ 0 & -4 \end{bmatrix}');

-- 5. Otra resta de matrices
INSERT INTO exercise (question, answer, type) VALUES 
('\begin{bmatrix} 7 & 4 \\ 2 & 8 \end{bmatrix} - \begin{bmatrix} 2 & 1 \\ 3 & 5 \end{bmatrix} = ?', '\begin{bmatrix} 5 & 3 \\ -1 & 3 \end{bmatrix}', 'MultipleChoiceExercise');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 7 & 4 \\ 2 & 8%'), '\begin{bmatrix} 5 & 3 \\ -1 & 3 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 7 & 4 \\ 2 & 8%'), '\begin{bmatrix} 5 & 3 \\ 1 & 3 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 7 & 4 \\ 2 & 8%'), '\begin{bmatrix} 9 & 5 \\ 5 & 13 \end{bmatrix}');

-- 6. Otra multiplicación de matrices
INSERT INTO exercise (question, answer, type) VALUES 
('\begin{bmatrix} 2 & 1 \\ 1 & 3 \end{bmatrix} \cdot \begin{bmatrix} 1 & 2 \\ 0 & 1 \end{bmatrix} = ?', '\begin{bmatrix} 2 & 5 \\ 1 & 5 \end{bmatrix}', 'MultipleChoiceExercise');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 2 & 1 \\ 1 & 3 \end{bmatrix} \cdot%'), '\begin{bmatrix} 2 & 5 \\ 1 & 5 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 2 & 1 \\ 1 & 3 \end{bmatrix} \cdot%'), '\begin{bmatrix} 2 & 3 \\ 1 & 5 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES 
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 2 & 1 \\ 1 & 3 \end{bmatrix} \cdot%'), '\begin{bmatrix} 3 & 5 \\ 3 & 6 \end{bmatrix}');