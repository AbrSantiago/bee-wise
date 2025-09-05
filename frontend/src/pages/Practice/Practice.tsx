import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import "katex/dist/katex.min.css";
// @ts-ignore
import { BlockMath } from "react-katex";
import "./Practice.css";
import apiClient from "../../services/api";
import { useParams } from "react-router-dom";
import {
  DndContext,
  closestCenter,
  PointerSensor,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import { Link } from "react-router-dom";
import type { DragEndEvent } from "@dnd-kit/core";

type Exercise = {
  id: number;
  question: string;
  answer: string;
  options: null | string[];
  type: string; // "OPEN" | "MULTIPLE_CHOICE"
};

export function PracticePage() {
  const { id } = useParams<{ id: string }>();
  const [exercises, setExercises] = useState<Exercise[]>([]);
  const [currentExercise, setCurrentExercise] = useState(0);
  const [userAnswer, setUserAnswer] = useState("");
  const [feedback, setFeedback] = useState<null | boolean>(null);
  const [canContinue, setCanContinue] = useState(false);
  const [pendingExercises, setPendingExercises] = useState<Exercise[]>([]);
  const [showSummary, setShowSummary] = useState(false);
  const [selectedOption, setSelectedOption] = useState<string | null>(null);

  const current = exercises[currentExercise];

  const getLesson = async () => {
    try {
      const response = await apiClient.get(`/lesson/${id}`);
      setExercises(response.data.exercises);
    } catch (error) {
      console.error("Error fetching practice data!", error);
    }
  };

  useEffect(() => {
    if (id) getLesson();
  }, [id]);

  // --- DnD setup ---
  const sensors = useSensors(useSensor(PointerSensor));

  const handleDragEnd = (event: DragEndEvent) => {
    const { active } = event;
    if (active?.id) {
      setUserAnswer(active.id.toString());
    }
  };

  const animateToSlot = (option: string, onFinish: () => void) => {
    const optionEl = document.getElementById(option);
    const answerSlot = document.querySelector(".answer-slot-container");

    if (optionEl && answerSlot) {
      const start = optionEl.getBoundingClientRect();
      const end = answerSlot.getBoundingClientRect();

      const clone = optionEl.cloneNode(true) as HTMLElement;
      clone.style.position = "absolute";
      clone.style.top = start.top + "px";
      clone.style.left = start.left + "px";
      clone.style.width = start.width + "px";
      clone.style.height = start.height + "px";
      clone.style.transition = "all 0.6s ease-in-out";
      clone.style.zIndex = "9999";
      document.body.appendChild(clone);

      requestAnimationFrame(() => {
        clone.style.top = end.top + "px";
        clone.style.left = end.left + "px";
        clone.style.width = end.width + "px";
        clone.style.height = end.height + "px";
        clone.style.opacity = "0.9";
      });

      clone.addEventListener("transitionend", () => {
        onFinish();
        clone.remove();
      });
    } else {
      onFinish();
    }
  };

  const animateBack = (option: string, onFinish: () => void) => {
    const optionEl = document.getElementById(option);
    const answerSlot = document.querySelector(".answer-slot-container");

    if (optionEl && answerSlot) {
      const end = optionEl.getBoundingClientRect();
      const start = answerSlot.getBoundingClientRect();

      const clone = optionEl.cloneNode(true) as HTMLElement;
      clone.style.position = "absolute";
      clone.style.top = start.top + "px";
      clone.style.left = start.left + "px";
      clone.style.width = start.width + "px";
      clone.style.height = start.height + "px";
      clone.style.transition = "all 0.6s ease-in-out";
      clone.style.zIndex = "9999";
      document.body.appendChild(clone);

      requestAnimationFrame(() => {
        clone.style.top = end.top + "px";
        clone.style.left = end.left + "px";
        clone.style.width = end.width + "px";
        clone.style.height = end.height + "px";
        clone.style.opacity = "1";
      });

      clone.addEventListener("transitionend", () => {
        onFinish();
        clone.remove();
      });
    } else {
      onFinish();
    }
  };

  const handleOptionClick = (option: string) => {
    if (selectedOption === option) return; // ya está seleccionada

    // si había una opción previa, la devolvemos
    if (selectedOption) {
      const prev = selectedOption;
      animateBack(prev, () => {
        setSelectedOption(null);
        setUserAnswer("");
      });
    }

    // animamos la nueva opción al slot
    animateToSlot(option, () => {
      setUserAnswer(option);
      setSelectedOption(option);
    });
  };

  const handleTrueFalseClick = (answer: string) => {
    setUserAnswer(answer);
  };

  const handleCheck = () => {
    const correct = userAnswer.trim() === current.answer.trim();
    setFeedback(correct);
    setCanContinue(true);
  };

  const handleContinue = () => {
    // Si fue incorrecto, lo agregamos a pendientes
    if (feedback === false) {
      setPendingExercises((prev) => [...prev, current]);
    }
    setUserAnswer("");
    setFeedback(null);
    setCanContinue(false);

    // Si quedan ejercicios, avanzamos
    if (currentExercise < exercises.length - 1) {
      setCurrentExercise((prev) => prev + 1);
    } else if (pendingExercises.length > 0) {
      // Si no quedan, pero hay pendientes, los mostramos
      setExercises(pendingExercises);
      setCurrentExercise(0);
      setPendingExercises([]);
    } else {
      // Si no quedan pendientes, mostramos resumen
      setShowSummary(true);
    }
  };

  if (showSummary) {
    return (
      <MainLayout title={`Lección ${id}`}>
        <div className="summary-container">
          <p className="summary-text">
            ¡Has terminado todos los ejercicios!
          </p>
          <Link to={`/`}>
            <button className="btn-back-home">
              <span>Volver al inicio</span>
            </button>
          </Link>
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout title={`Lección ${id}`}>
      {current ? (
        <>
          {current.type === "OPEN" ? (
            <div className="mt-4">
              <div className="matrix-container">
                <p className="question-text">{current.question}</p>
              </div>
              <div className="button-true-false">
                <button
                  className={`true-false-btn verdadero-btn ${feedback === false && userAnswer === "Verdadero"
                    ? "incorrect"
                    : ""
                    }`}
                  onClick={() => {
                    handleTrueFalseClick("Verdadero");
                    const correct =
                      "Verdadero".trim() === current.answer.trim();
                    setFeedback(correct);
                    setCanContinue(true);
                  }}
                  disabled={canContinue}
                >
                  Verdadero
                </button>
                <button
                  className={`true-false-btn falso-btn ${feedback === false && userAnswer === "Falso"
                    ? "incorrect"
                    : ""
                    }`}
                  onClick={() => {
                    handleTrueFalseClick("Falso");
                    const correct = "Falso".trim() === current.answer.trim();
                    setFeedback(correct);
                    setCanContinue(true);
                  }}
                  disabled={canContinue}
                >
                  Falso
                </button>
              </div>
            </div>
          ) : (
            <DndContext
              sensors={sensors}
              collisionDetection={closestCenter}
              onDragEnd={handleDragEnd}
            >
              <div className="question-and-answer-container">
                <div className="matrix-container">
                  <BlockMath math={current.question.replace(/\?$/, "")} />
                </div>
                <div className="answer-slot-container mt-4">
                  {userAnswer ? <BlockMath math={userAnswer} /> : <></>}
                </div>
              </div>
              <div className="options-container mt-4 flex gap-2 flex-wrap">
                {current.options?.map((opt) => (
                  <div
                    key={opt}
                    id={opt}
                    draggable
                    className={`option-box cursor-pointer ${canContinue ? "disabled" : ""}`}
                    onClick={() => !canContinue && handleOptionClick(opt)}
                    style={{
                      pointerEvents:
                        canContinue || selectedOption === opt ? "none" : "auto",
                      opacity:
                        canContinue || selectedOption === opt ? 0.6 : 1,
                    }}
                  >
                    <BlockMath math={opt} />
                  </div>
                ))}
              </div>
              <button
                onClick={handleCheck}
                className="check-btn"
                disabled={canContinue || !userAnswer}
              >
                Check
              </button>
            </DndContext>
          )}

          {feedback !== null && (
            <>
              {feedback ? (
                <div className="feedback-message success">
                  <span>¡Excelente! Seguí así</span>
                </div>
              ) : (
                <div className="feedback-message error">
                  <span>Mmm, nop. Esa no era, pero no te rindas!</span>
                </div>
              )}
              <button
                className={`btn-continue mt-4 ${feedback ? "success" : "error"
                  }`}
                onClick={handleContinue}
              >
                Continuar
              </button>
            </>
          )}
        </>
      ) : (
        <p className="text-gray-500">Cargando ejercicio...</p>
      )}
    </MainLayout>
  );
}
