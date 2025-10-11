-- Ejercicios de tipo OPEN (Verdadero/Falso) sobre teoría de álgebra lineal
INSERT INTO exercise (question, answer, type, category) VALUES
('La multiplicación de matrices es conmutativa', 'Falso', 'OpenExercise', 'MATRICES'),
('El determinante de una matriz triangular superior es igual al producto de sus elementos diagonales', 'Verdadero', 'OpenExercise', 'MATRICES'),
('Si una matriz tiene una fila de ceros, entonces su determinante es cero', 'Verdadero', 'OpenExercise', 'MATRICES'),
('La suma de matrices es asociativa', 'Verdadero', 'OpenExercise', 'MATRICES'),
('Toda matriz cuadrada tiene inversa', 'Falso', 'OpenExercise', 'MATRICES'),
('El rango de una matriz es siempre menor o igual al mínimo entre el número de filas y columnas', 'Verdadero', 'OpenExercise', 'MATRICES'),
('La matriz identidad multiplicada por cualquier matriz cuadrada del mismo orden da como resultado la misma matriz', 'Verdadero', 'OpenExercise', 'MATRICES');

-- Ejercicios de tipo MULTIPLE_CHOICE

INSERT INTO exercise (question, answer, type, category) VALUES
('Calcule: \begin{bmatrix} 2 & 0 \\ -1 & 3 \end{bmatrix} + \begin{bmatrix} 4 & 1 \\ 0 & -2 \end{bmatrix} = ?', '\begin{bmatrix} 6 & 1 \\ -1 & 1 \end{bmatrix}', 'MultipleChoiceExercise', 'MATRICES'),
('\begin{bmatrix} 2 & 0 \\ -1 & 3 \end{bmatrix} + \begin{bmatrix} 4 & 1 \\ 0 & -2 \end{bmatrix} = ', '\begin{bmatrix} 6 & 1 \\ -1 & 1 \end{bmatrix}', 'MultipleChoiceExercise', 'MATRICES');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE 'Calcule: \\begin{bmatrix} 2 & 0%'), '\begin{bmatrix} 6 & 1 \\ -1 & 1 \end{bmatrix}'),
((SELECT id FROM exercise WHERE question LIKE 'Calcule: \\begin{bmatrix} 2 & 0%'), '\begin{bmatrix} 5 & 1 \\ -1 & 2 \end{bmatrix}'),
((SELECT id FROM exercise WHERE question LIKE 'Calcule: \\begin{bmatrix} 2 & 0%'), '\begin{bmatrix} 6 & 2 \\ -1 & 0 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\\begin{bmatrix} 2 & 0%'), '\begin{bmatrix} 6 & 1 \\ -1 & 1 \end{bmatrix}'),
((SELECT id FROM exercise WHERE question LIKE '\\begin{bmatrix} 2 & 0%'), '\begin{bmatrix} 5 & 1 \\ -1 & 2 \end{bmatrix}'),
((SELECT id FROM exercise WHERE question LIKE '\\begin{bmatrix} 2 & 0%'), '\begin{bmatrix} 6 & 2 \\ -1 & 0 \end{bmatrix}');

-- 1. Multiplicación de matriz por escalar
INSERT INTO exercise (question, answer, type, category) VALUES
('Calcule: 3 \cdot \begin{bmatrix} 2 & 1 \\ 4 & 3 \end{bmatrix} = ?', '\begin{bmatrix} 6 & 3 \\ 12 & 9 \end{bmatrix}', 'MultipleChoiceExercise', 'MATRICES');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE 'Calcule: 3 \cdot \begin{bmatrix} 2 & 1%'), '\begin{bmatrix} 6 & 3 \\ 12 & 9 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE 'Calcule: 3 \cdot \begin{bmatrix} 2 & 1%'), '\begin{bmatrix} 5 & 4 \\ 7 & 6 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE 'Calcule: 3 \cdot \begin{bmatrix} 2 & 1%'), '\begin{bmatrix} 6 & 4 \\ 12 & 8 \end{bmatrix}');

-- 2. Resta de matrices
INSERT INTO exercise (question, answer, type, category) VALUES
('\begin{bmatrix} 8 & 5 \\ 6 & 9 \end{bmatrix} - \begin{bmatrix} 3 & 2 \\ 1 & 4 \end{bmatrix} = ?', '\begin{bmatrix} 5 & 3 \\ 5 & 5 \end{bmatrix}', 'MultipleChoiceExercise', 'MATRICES');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 8 & 5%'), '\begin{bmatrix} 5 & 3 \\ 5 & 5 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 8 & 5%'), '\begin{bmatrix} 5 & 3 \\ 7 & 5 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 8 & 5%'), '\begin{bmatrix} 11 & 7 \\ 7 & 13 \end{bmatrix}');

-- 3. Multiplicación de matrices
INSERT INTO exercise (question, answer, type, category) VALUES
('\begin{bmatrix} 1 & 2 \\ 3 & 4 \end{bmatrix} \cdot \begin{bmatrix} 2 & 0 \\ 1 & 3 \end{bmatrix} = ?', '\begin{bmatrix} 4 & 6 \\ 10 & 12 \end{bmatrix}', 'MultipleChoiceExercise', 'MATRICES');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 1 & 2 \\ 3 & 4 \end{bmatrix} \cdot%'), '\begin{bmatrix} 4 & 6 \\ 10 & 12 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 1 & 2 \\ 3 & 4 \end{bmatrix} \cdot%'), '\begin{bmatrix} 2 & 6 \\ 7 & 12 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 1 & 2 \\ 3 & 4 \end{bmatrix} \cdot%'), '\begin{bmatrix} 4 & 5 \\ 10 & 11 \end{bmatrix}');

-- 4. Otro ejercicio de multiplicación por escalar
INSERT INTO exercise (question, answer, type, category) VALUES
('Calcule: -2 \cdot \begin{bmatrix} 3 & -1 \\ 0 & 2 \end{bmatrix} = ?', '\begin{bmatrix} -6 & 2 \\ 0 & -4 \end{bmatrix}', 'MultipleChoiceExercise', 'MATRICES');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE 'Calcule: -2 \cdot \begin{bmatrix} 3 & -1%'), '\begin{bmatrix} -6 & 2 \\ 0 & -4 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE 'Calcule: -2 \cdot \begin{bmatrix} 3 & -1%'), '\begin{bmatrix} 6 & -2 \\ 0 & 4 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE 'Calcule: -2 \cdot \begin{bmatrix} 3 & -1%'), '\begin{bmatrix} -6 & -2 \\ 0 & -4 \end{bmatrix}');

-- 5. Otra resta de matrices
INSERT INTO exercise (question, answer, type, category) VALUES
('\begin{bmatrix} 7 & 4 \\ 2 & 8 \end{bmatrix} - \begin{bmatrix} 2 & 1 \\ 3 & 5 \end{bmatrix} = ?', '\begin{bmatrix} 5 & 3 \\ -1 & 3 \end{bmatrix}', 'MultipleChoiceExercise', 'MATRICES');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 7 & 4 \\ 2 & 8%'), '\begin{bmatrix} 5 & 3 \\ -1 & 3 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 7 & 4 \\ 2 & 8%'), '\begin{bmatrix} 5 & 3 \\ 1 & 3 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 7 & 4 \\ 2 & 8%'), '\begin{bmatrix} 9 & 5 \\ 5 & 13 \end{bmatrix}');

-- 6. Otra multiplicación de matrices
INSERT INTO exercise (question, answer, type, category) VALUES
('\begin{bmatrix} 2 & 1 \\ 1 & 3 \end{bmatrix} \cdot \begin{bmatrix} 1 & 2 \\ 0 & 1 \end{bmatrix} = ?', '\begin{bmatrix} 2 & 5 \\ 1 & 5 \end{bmatrix}', 'MultipleChoiceExercise', 'MATRICES');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 2 & 1 \\ 1 & 3 \end{bmatrix} \cdot%'), '\begin{bmatrix} 2 & 5 \\ 1 & 5 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 2 & 1 \\ 1 & 3 \end{bmatrix} \cdot%'), '\begin{bmatrix} 2 & 3 \\ 1 & 5 \end{bmatrix}');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE '\begin{bmatrix} 2 & 1 \\ 1 & 3 \end{bmatrix} \cdot%'), '\begin{bmatrix} 3 & 5 \\ 3 & 6 \end{bmatrix}');


------------------------------------------------
-- 3. SYSTEM OF EQUATIONS
------------------------------------------------

INSERT INTO exercise (question, answer, type, category) VALUES
('¿Todo sistema de ecuaciones lineales tiene solución?', 'Falso', 'OpenExercise', 'SYSTEM_OF_EQUATIONS');

INSERT INTO exercise (question, answer, type, category) VALUES
('Resuelva el sistema: \n x + y = 3 \n x - y = 1', '(x, y) = (2, 1)', 'MultipleChoiceExercise', 'SYSTEM_OF_EQUATIONS');

INSERT INTO multiple_choice_exercise_options (multiple_choice_exercise_id, options) VALUES
((SELECT id FROM exercise WHERE question LIKE 'Resuelva el sistema:%x + y = 3%'), '(x, y) = (2, 1)'),
((SELECT id FROM exercise WHERE question LIKE 'Resuelva el sistema:%x + y = 3%'), '(x, y) = (1, 2)'),
((SELECT id FROM exercise WHERE question LIKE 'Resuelva el sistema:%x + y = 3%'), '(x, y) = (3, 0)');

INSERT INTO exercise (question, answer, type, category) VALUES
('¿Un sistema homogéneo siempre tiene al menos una solución?', 'Verdadero', 'OpenExercise', 'SYSTEM_OF_EQUATIONS');


