import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
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
import ChallengeUserCard from "./ChallengeUserCard";
import { useUser } from "../../context/UserContext";
import ChallengeSummary from "./ChallengeSummary";
import Confetti from "../../components/layout/Confetti";
import RouletteScreen from "./RouletteScreen";
import dailyMissionService, {
  type DailyMissionUpdateOutDTO,
} from "../../services/dailyMissionService";
import MissionsUpdateSummary from "../../components/layout/MissionsUpdateSummary";
import { useOpponent } from "../../hooks/useOpponent";

export function ChallengePlayPage() {
  const [currentExercise, setCurrentExercise] = useState(0);
  const [userAnswer, setUserAnswer] = useState("");
  const [feedback, setFeedback] = useState<null | boolean>(null);
  const [canContinue, setCanContinue] = useState(false);
  const [pendingExercises, setPendingExercises] = useState<Exercise[]>([]);
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
  const [summaryDataLoaded, setSummaryDataLoaded] = useState(false);
  const [missionsUpdate, setMissionsUpdate] = useState<
    DailyMissionUpdateOutDTO[] | null
  >(null);
  const [pendingMissionsUpdate, setPendingMissionsUpdate] = useState<
    DailyMissionUpdateOutDTO[] | null
  >(null);

  const { user } = useUser();
  const navigate = useNavigate();

  const [gameState, setGameState] = useState<
    | "ROULETTE"
    | "PLAYING"
    | "CHALLENGE_SUMMARY"
    | "ROUND_SUMMARY"
    | "MISSION_SUMMARY"
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
  const current = exercises[currentExercise];
  const sensors = useSensors(useSensor(PointerSensor));

  const { opponent, loadingOpponent } = useOpponent(
    challengeId,
    user?.username
  );

  // --- TUS FUNCIONES DE JUEGO ORIGINALES (INTACTAS) ---
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
      setGameState("ROUND_SUMMARY");
      handleSubmitTurn();
    }
  };

  const handleTimeOut = () => {
    setFeedback(false);
    setCanContinue(true);
    setIsTimeOut(true);
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
        score: correctCount,
        correctAnswers: correctCount,
      };

      const challenge = await challengeService.answerRound(answerDTO);

      if (challenge.status === "COMPLETED") {
        const currentUserIsWinner =
          (rol === "CHALLENGER" && challenge.result === "CHALLENGER_WIN") ||
          (rol === "CHALLENGED" && challenge.result === "CHALLENGED_WIN");

        setIsWinner(currentUserIsWinner);
        setSummaryDataLoaded(false); // Resetear cuando cambiamos de estado

        if (currentUserIsWinner) {
          const missionsResult = await dailyMissionService.updateProgress([
            { type: "PLAY_CHALLENGE", progressAmount: 1 },
            { type: "WIN_CHALLENGE", progressAmount: 1 },
          ]);
          setMissionsUpdate(missionsResult);
          if (missionsResult[0].wasUpdated || missionsResult[1].wasUpdated)
            setPendingMissionsUpdate(missionsResult);
        } else {
          const missionsResult = await dailyMissionService.updateProgress([
            { type: "PLAY_CHALLENGE", progressAmount: 1 },
          ]);
          setMissionsUpdate(missionsResult);
          if (missionsResult[0].wasUpdated)
            setPendingMissionsUpdate(missionsResult);
        }

        setTimeout(() => {
          setGameState("CHALLENGE_SUMMARY");
        }, 500);
      }
    } catch (error) {
      console.error("Error submitting turn:", error);
    }
  };

  useEffect(() => {
    if (gameState === "CHALLENGE_SUMMARY" && isWinner && summaryDataLoaded) {
      const timer = setTimeout(() => {
        setShowConfetti(true);

        setTimeout(() => {
          setShowConfetti(false);
        }, 8000);
      }, 300);

      return () => clearTimeout(timer);
    }
  }, [gameState, isWinner, summaryDataLoaded]);

  useEffect(() => {
    if (gameState !== "CHALLENGE_SUMMARY") {
      setShowConfetti(false);
      setSummaryDataLoaded(false);
    }
  }, [gameState]);

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
    }
  }, [currentExercise, current, loading, gameState]);

  // --- RENDERIZADO FINAL ---

  if (gameState === "ROULETTE") {
    // if (loading)
    //   return <MainLayout title="Cargando...">Cargando desafío...</MainLayout>;
    return (
      <RouletteScreen
        loading={loading}
        categories={rouletteCategories}
        winningCategory={winningCategory}
        isSpinning={isSpinning}
        showCategoryPopup={showCategoryPopup}
        onSpinStart={handleStartSpin}
        onSpinEnd={handleSpinEnd}
        onStartGame={handleStartGame}
      />
    );
  }

  if (gameState === "ROUND_SUMMARY") {
    return (
      <div className="challenge-round-summary-container">
        <SummaryScreen
          time={endTime && startTime ? endTime - startTime : 0}
          correctCount={correctCount}
          totalCount={totalCount}
          overrideButton={pendingMissionsUpdate ? "Siguiente" : undefined}
          onContinue={() => {
            if (pendingMissionsUpdate) {
              setGameState("MISSION_SUMMARY");
              setMissionsUpdate(pendingMissionsUpdate);
              setPendingMissionsUpdate(null);
            } else {
              navigate("/");
            }
          }}
        />
      </div>
    );
  }

  if (gameState == "MISSION_SUMMARY" && missionsUpdate) {
    return (
      <MissionsUpdateSummary
        missions={missionsUpdate}
        onFinish={() => {
          navigate("/");
        }}
      />
    );
  }

  if (gameState === "CHALLENGE_SUMMARY") {
    return (
      <div>
        {showConfetti && <Confetti duration={8000} intensity="high" />}
        <ChallengeSummary
          challengeId={challengeId}
          onDataLoaded={() => setSummaryDataLoaded(true)}
        />
      </div>
    );
  }

  // gameState es "PLAYING"
  if (loading || loadingOpponent)
    return <MainLayout title="Cargando...">Cargando ejercicios...</MainLayout>;

  return (
    user &&
    opponent && (
      <div className="challenge-play-container">
        <ChallengeUserCard user={user} isCurrentUser={true} />
        <div className="challenge-exercise-container">
          <div
            className={`timer-container ${
              timeLeft <= 10 ? "timer-warning" : ""
            }`}
          >
            <div
              className={`timer-display ${timeLeft <= 10 ? "timer-pulse" : ""}`}
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
        <ChallengeUserCard user={opponent} isCurrentUser={false} />
      </div>
    )
  );
}
