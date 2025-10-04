import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import "katex/dist/katex.min.css";
// @ts-ignore
import { BlockMath } from "react-katex";
import "./Practice.css";
import { useParams } from "react-router-dom";
import { DndContext, closestCenter, PointerSensor, useSensor, useSensors, } from "@dnd-kit/core";
import type { DragEndEvent } from "@dnd-kit/core";
import lessonService, { type Exercise } from "../../services/lessonService";
import { useUserPoints } from "../../context/UserPointsContext";
import TrueFalseButtons from "./components/TrueFalseButtons";
import DnDOptions from "./components/DnDOptions";
import FeedbackMessage from "./components/FeedbackMessage";
import SummaryScreen from "./components/SummaryScreen";
import CorrectionIntroScreen from "./components/CorrectionIntroScreen";

export function PracticePage() {
  const { id } = useParams<{ id: string }>();
  // const { user } = useUser();
  const { userPoints,refreshPoints } = useUserPoints();
  const [exercises, setExercises] = useState<Exercise[]>([]);
  const [currentExercise, setCurrentExercise] = useState(0);
  const [userAnswer, setUserAnswer] = useState("");
  const [feedback, setFeedback] = useState<null | boolean>(null);
  const [canContinue, setCanContinue] = useState(false);
  const [pendingExercises, setPendingExercises] = useState<Exercise[]>([]);
  const [showSummary, setShowSummary] = useState(false);
  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const [showCorrectionIntro, setShowCorrectionIntro] = useState(false);
  const [hasShownCorrectionIntro, setHasShownCorrectionIntro] = useState(false);
  const [startTime, setStartTime] = useState<number | null>(null);
  const [endTime, setEndTime] = useState<number | null>(null);
  const [correctCount, setCorrectCount] = useState(0);
  const [totalCount, setTotalCount] = useState(0);
  const [inCorrectionRound, setInCorrectionRound] = useState(false);
  const [lessonCompleted, setLessonCompleted] = useState(false);

  const current = exercises[currentExercise];

  useEffect(() => {
    const fetchLesson = async () => {
      try {
        if (id) {
          const lesson = await lessonService.getLesson(id);
          console.log("2 - Agarro una lesson:", lesson);
          
          setExercises(lesson.exercises);
          setTotalCount(lesson.exercises.length);
          setCorrectCount(0);
          setStartTime(Date.now());
        }
      } catch (error) {
        console.error("Error fetching practice data!", error);
      }
    };
    fetchLesson();
  }, [id]);

  useEffect(() => {
    const userId = userPoints?.userId;

    const completeLessonCall = async () => {
      if (showSummary && !lessonCompleted && id && userId) {
        try {
          setLessonCompleted(true);
          
          const response = await lessonService.lessonComplete({
            completedLessonId: parseInt(id),
            userId: userId,
            correctExercises: correctCount
          });
          await refreshPoints();
        } catch (error) {
          console.error("❌ Error completing lesson:", error);
          setLessonCompleted(false);
        }
      }
    };

    completeLessonCall();
  }, [showSummary, lessonCompleted, id, userPoints?.userId, correctCount, refreshPoints]);

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
    if (correct && !inCorrectionRound) setCorrectCount((prev) => prev + 1);
  };

  const handleContinue = () => {
    let newPending = [...pendingExercises];
    if (feedback === false) {
      newPending.push(current);
    }

    // Reiniciamos estados del ejercicio actual
    setUserAnswer("");
    setSelectedOption(null);
    setFeedback(null);
    setCanContinue(false);

    if (currentExercise < exercises.length - 1) {
      setCurrentExercise((prev) => prev + 1);
      setPendingExercises(newPending);
    } else if (newPending.length > 0) {
      if (!hasShownCorrectionIntro) {
        setShowCorrectionIntro(true);
        setHasShownCorrectionIntro(true);
        setPendingExercises(newPending);
        setInCorrectionRound(true);
      } else {
        setExercises(newPending);
        setCurrentExercise(0);
        setPendingExercises([]);
      }
    } else {
      setEndTime(Date.now());
      setShowSummary(true);
    }
  };

  if (showSummary) {
    return (
      <MainLayout title={`Lección ${id}`}>
        <SummaryScreen
          time={endTime && startTime ? endTime - startTime : 0}
          correctCount={correctCount}
          totalCount={totalCount}
        />
      </MainLayout>
    );
  }

  if (showCorrectionIntro) {
    return (
      <MainLayout title={`Lección ${id}`}>
        <CorrectionIntroScreen
          lessonId={id}
          onContinue={() => {
            setExercises(pendingExercises);
            setCurrentExercise(0);
            setPendingExercises([]);
            setShowCorrectionIntro(false);
          }}
        />
      </MainLayout>
    );
  }

  return (
    <MainLayout title={`Lección ${id}`}>
      <div className="exercise-container">
        {current ? (
          <>
            {current.type === "OPEN" ? (
              <div className="mt-4">
                <div className="matrix-container">
                  <p className="question-text">{current.question}</p>
                </div>
                <TrueFalseButtons
                  userAnswer={userAnswer}
                  feedback={feedback}
                  canContinue={canContinue}
                  onClick={(answer) => {
                    handleTrueFalseClick(answer);
                    const correct = answer.trim() === current.answer.trim();
                    setFeedback(correct);
                    setCanContinue(true);
                    if (correct && !inCorrectionRound) setCorrectCount((prev) => prev + 1);
                  }}
                />
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
                <DnDOptions
                  options={current.options || []}
                  canContinue={canContinue}
                  selectedOption={selectedOption}
                  handleOptionClick={handleOptionClick}
                  userAnswer={userAnswer}
                  feedback={feedback}
                  current={current}
                />
                <button
                  onClick={handleCheck}
                  className="check-btn"
                  disabled={canContinue || !userAnswer}
                >
                  Check
                </button>
              </DndContext>
            )}

            <FeedbackMessage feedback={feedback} />

            {feedback !== null && (
              <button
                className={`btn-continue mt-4 ${feedback ? "success" : "error"}`}
                onClick={handleContinue}
              >
                Continuar
              </button>
            )}
          </>
        ) : (
          <p className="text-gray-500">Cargando ejercicio...</p>
        )}
      </div>
    </MainLayout>
  );
}
