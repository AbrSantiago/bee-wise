import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout";
import challengeService, {
  type AnswerDTO,
  type ChallengeRol,
  type ExerciseCategory,
} from "../../services/challengeService";
import type { Exercise } from "../../services/lessonService";
import SummaryScreen from "../Practice/components/SummaryScreen";
import TrueFalseButtons from "../Practice/components/TrueFalseButtons";
import "katex/dist/katex.min.css";
// @ts-ignore
import { BlockMath } from "react-katex";
import "../Practice/Practice.css";
import "./ChallengePlay.css";
import {
  DndContext,
  closestCenter,
  PointerSensor,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import type { DragEndEvent } from "@dnd-kit/core";
import FeedbackMessage from "../Practice/components/FeedbackMessage";
import DnDOptions from "../Practice/components/DnDOptions";
import Roulette from "../../components/layout/Roulette";
import CategoryPopup from "../../components/layout/CategoryPopup";
import ChallengeUserCard from "./ChallengeUserCard";
import { useUser } from "../../context/UserContext";
import OpponentCard from "./OpponentCard";
import Confetti from "../../components/layout/Confetti";

export function ChallengePlayPage() {
  const [currentExercise, setCurrentExercise] = useState(0);
  const [userAnswer, setUserAnswer] = useState("");
  const [feedback, setFeedback] = useState<null | boolean>(null);
  const [canContinue, setCanContinue] = useState(false);
  const [pendingExercises, setPendingExercises] = useState<Exercise[]>([]);
  const [showSummary, setShowSummary] = useState(false); // Mantendremos este por ahora para la lógica final
  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const [startTime, setStartTime] = useState<number | null>(null);
  const [endTime, setEndTime] = useState<number | null>(null);
  const [correctCount, setCorrectCount] = useState(0);
  const [totalCount, setTotalCount] = useState(0);
  const [exercises, setExercises] = useState<Exercise[]>([]);
  const [loading, setLoading] = useState(true); // Lo usaremos para la carga de ejercicios post-ruleta
  const [timeLeft, setTimeLeft] = useState(25);
  const [isTimerRunning, setIsTimerRunning] = useState(false);
  const [isTimeOut, setIsTimeOut] = useState(false);
  const [showCategoryPopup, setShowCategoryPopup] = useState(false);
  const [showConfetti, setShowConfetti] = useState(false);
  const [isWinner, setIsWinner] = useState(false);
  const { user } = useUser();

  // --- Estados nuevos para la ruleta y el flujo de juego ---
  const [gameState, setGameState] = useState<
    "ROULETTE" | "PLAYING" | "SUMMARY"
  >("ROULETTE");
  const [isSpinning, setIsSpinning] = useState(false);
  const [winningCategory, setWinningCategory] =
    useState<ExerciseCategory | null>(null);
  const [rouletteCategories, setRouletteCategories] = useState<
    ExerciseCategory[]
  >([]);

  const { challengeId, roundNumber, questionsPerRound, rol } = useParams<{
    challengeId: string;
    roundNumber: string;
    questionsPerRound: string;
    rol: ChallengeRol;
  }>();
  const navigate = useNavigate();
  const current = exercises[currentExercise];
  const sensors = useSensors(useSensor(PointerSensor));

  // --- TUS FUNCIONES DE JUEGO ORIGINALES (INTACTAS) ---
  const handleDragEnd = (event: DragEndEvent) => {
    const { active } = event;
    if (active?.id) {
      setUserAnswer(active.id.toString());
    }
  };

  const animateToSlot = (option: string, onFinish: () => void) => {
    // ... tu código de animación ...
  };

  const animateBack = (option: string, onFinish: () => void) => {
    // ... tu código de animación ...
  };

  const handleOptionClick = (option: string) => {
    if (selectedOption === option) return;
    if (selectedOption) {
      const prev = selectedOption;
      animateBack(prev, () => {
        setSelectedOption(null);
        setUserAnswer("");
      });
    }
    animateToSlot(option, () => {
      setUserAnswer(option);
      setSelectedOption(option);
    });
  };

  const handleTrueFalseClick = (answer: string) => {
    setUserAnswer(answer);
  };

  const handleCheck = () => {
    setIsTimerRunning(false);
    setIsTimeOut(false);
    const correct = userAnswer.trim() === current.answer.trim();
    setFeedback(correct);
    setCanContinue(true);
    if (correct) setCorrectCount((prev) => prev + 1);
  };

  const handleContinue = () => {
    let newPending = [...pendingExercises];
    if (feedback === false) {
      newPending.push(current);
    }
    setUserAnswer("");
    setSelectedOption(null);
    setFeedback(null);
    setCanContinue(false);
    setIsTimeOut(false);

    if (currentExercise < exercises.length - 1) {
      setCurrentExercise((prev) => prev + 1);
      setPendingExercises(newPending);
    } else {
      setEndTime(Date.now());
      setGameState("SUMMARY"); // <-- ÚNICO CAMBIO: Usamos gameState en lugar de showSummary
      handleSubmitTurn();
    }
  };

  const handleTimeOut = () => {
    setFeedback(false);
    setCanContinue(true);
    setIsTimeOut(true);
    console.log("⏰ Tiempo agotado para la pregunta");
  };

  const handleSubmitTurn = async () => {
    try {
      if (!rol) {
        console.error("Rol is missing in params");
        return;
      }

      const answerDTO: AnswerDTO = {
        challengeId: Number(challengeId),
        roundNumber: Number(roundNumber),
        rol: rol,
        score: correctCount * 2,
        correctAnswers: correctCount,
      };

      const challenge = await challengeService.answerRound(answerDTO);

      if (challenge.status === "COMPLETED") {
        const currentUserIsWinner =
          (rol === "CHALLENGER" && challenge.result === "CHALLENGER_WIN") ||
          (rol === "CHALLENGED" && challenge.result === "CHALLENGED_WIN");

        if (currentUserIsWinner) {
          setIsWinner(true);
          setShowConfetti(true);

          // Ocultar confetis después de 5 segundos
          setTimeout(() => {
            setShowConfetti(false);
          }, 5000);
        }

        setTimeout(
          () => {
            alert(`Challenge completed! Result: ${challenge.result}`);
            navigate("/challenges");
          },
          currentUserIsWinner ? 2000 : 0
        );
      } else {
        alert("Turn submitted! Next player notified.");
        navigate("/challenges");
      }
    } catch (error) {
      console.error("Error submitting turn:", error);
    }
  };

  // --- LÓGICA DE LA RULETA ---
  useEffect(() => {
    setLoading(true); // Mostramos un loading general al principio
    const fetchCategories = async () => {
      try {
        const categories = await challengeService.getAllCategories();
        setRouletteCategories(categories);
      } catch (error) {
        console.error("No se pudieron cargar las categorías:", error);
        setRouletteCategories([
          "MATRICES",
          "DETERMINANTS",
          "SYSTEM_OF_EQUATIONS",
        ]); // Fallback
      } finally {
        setLoading(false); // Dejamos de cargar cuando las categorías están listas
      }
    };
    fetchCategories();
  }, []);

  const handleStartSpin = () => {
    if (!isSpinning) {
      // Obtener categoría aleatoria
      challengeService
        .getRandomCategory()
        .then((category) => {
          setWinningCategory(category);
          setIsSpinning(true);
        })
        .catch((error) => {
          console.error("Error obteniendo categoría aleatoria:", error);
        });
    }
  };

  const handleSpinEnd = () => {
    console.log("🎉 Ruleta detenida en categoría:", winningCategory);
    setIsSpinning(false);
    setShowCategoryPopup(true); // Mostrar el popup cuando termine de girar
  };

  const handleStartGame = async () => {
    if (!winningCategory) return;

    setShowCategoryPopup(false); // Ocultar el popup
    setLoading(true); // Ponemos el loading mientras se buscan los ejercicios

    try {
      const data = await challengeService.getRandomExercises(
        Number(questionsPerRound),
        winningCategory
      );
      setExercises(data);
      setTotalCount(data.length);
      setCorrectCount(0);
      setStartTime(Date.now());
      setGameState("PLAYING");
    } catch (error) {
      console.error("Error fetching challenge exercises:", error);
    } finally {
      setLoading(false); // Quitamos el loading
    }
  };

  // --- TUS useEffect ORIGINALES PARA EL TIMER ---
  useEffect(() => {
    let interval: NodeJS.Timeout | null = null;

    if (isTimerRunning && timeLeft > 0) {
      interval = setInterval(() => {
        setTimeLeft((prev) => {
          if (prev <= 1) {
            setIsTimerRunning(false);
            handleTimeOut();
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isTimerRunning, timeLeft]);

  useEffect(() => {
    if (gameState === "PLAYING" && current && !loading) {
      setTimeLeft(25);
      setIsTimerRunning(true);
      console.log("🚀 Timer iniciado para nueva pregunta");
    }
  }, [currentExercise, current, loading, gameState]);

  // --- RENDERIZADO FINAL ---

  if (gameState === "ROULETTE") {
    if (loading)
      return <MainLayout title="Cargando...">Cargando desafío...</MainLayout>;
    return (
      <MainLayout title={`Desafío`}>
        <div
          style={{
            textAlign: "center",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <h2
            style={{
              color: "#fff",
              fontFamily: "Recoleta-Bold",
              fontSize: "2.5rem",
              marginBottom: "20px",
            }}
          >
            ¡Gira para definir la categoría!
          </h2>
          <Roulette
            categories={rouletteCategories}
            winningCategory={winningCategory}
            triggerSpin={isSpinning}
            onSpinningEnd={handleSpinEnd}
          />
          <button
            onClick={handleStartSpin}
            className="check-btn"
            disabled={isSpinning}
            style={{
              marginTop: "30px",
              width: "250px",
              fontSize: "1.5rem",
              padding: "15px",
            }}
          >
            {isSpinning ? "Girando..." : "¡GIRAR!"}
          </button>
        </div>

        {/* Mostrar el popup cuando showCategoryPopup sea true */}
        {showCategoryPopup && winningCategory && (
          <CategoryPopup category={winningCategory} onStart={handleStartGame} />
        )}
      </MainLayout>
    );
  }

  if (gameState === "SUMMARY") {
    return (
      <MainLayout title={`Resumen del Desafío`}>
        {showConfetti && <Confetti duration={5000} />}
        <SummaryScreen
          time={endTime && startTime ? endTime - startTime : 0}
          correctCount={correctCount}
          totalCount={totalCount}
        />
      </MainLayout>
    );
  }

  // gameState es "PLAYING"
  if (loading)
    return <MainLayout title="Cargando...">Cargando ejercicios...</MainLayout>;

  return (
    user && (
      <MainLayout title={`Desafío - Ronda ${roundNumber}`}>
        <div className="challenge-play-container">
          <ChallengeUserCard user={user} isCurrentUser={true} />
          <div className="exercise-container">
            {/* Aquí va tu JSX original del juego, sin cambios */}
            <div
              className={`timer-container ${
                timeLeft <= 10 ? "timer-warning" : ""
              }`}
            >
              <div
                className={`timer-display ${
                  timeLeft <= 10 ? "timer-pulse" : ""
                }`}
              >
                Tiempo restante : {timeLeft}s
              </div>
            </div>
            {current ? (
              <>
                {/* TU LÓGICA DE PREGUNTAS OPEN Y DnD */}
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
                        setIsTimerRunning(false);
                        setIsTimeOut(false);
                        handleTrueFalseClick(answer);
                        const correct = answer.trim() === current.answer.trim();
                        setFeedback(correct);
                        setCanContinue(true);
                        if (correct) setCorrectCount((prev) => prev + 1);
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
                <FeedbackMessage feedback={feedback} isTimeOut={isTimeOut} />
                {feedback !== null && (
                  <button
                    className={`btn-continue mt-4 ${
                      feedback ? "success" : "error"
                    }`}
                    onClick={handleContinue}
                  >
                    Continuar
                  </button>
                )}
              </>
            ) : (
              <p className="text-gray-500">No se encontraron ejercicios.</p>
            )}
          </div>
          <OpponentCard challengeId={challengeId} username={user.username} />
        </div>
      </MainLayout>
    )
  );
}
