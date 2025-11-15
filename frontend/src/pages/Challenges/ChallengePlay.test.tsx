import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { ChallengePlayPage } from "./ChallengePlay";
import challengeService from "../../services/challengeService";

// Mock del contexto de autenticación
vi.mock("../../context/AuthContext", () => ({
  useAuth: () => ({
    user: {
      id: 1,
      username: "testuser",
      email: "test@test.com",
      firstName: "Test",
      lastName: "User",
    },
    token: "mock-token",
    login: vi.fn(),
    logout: vi.fn(),
    isAuthenticated: true,
  }),
}));

// Mock del contexto de usuario con avatar
const mockUser = {
  id: 1,
  username: "testuser",
  email: "test@test.com",
  firstName: "Test",
  lastName: "User",
  progress: { totalXp: 0, level: 1, lessonsCompleted: 0 },
  avatar: {
    background: { image: "bg1.png", color: "#ffffff" },
    body: { image: "body1.png", color: "#ffddcc" },
    eyes: { image: "eyes1.png", color: "#000000" },
    mouth: { image: "mouth1.png", color: "#ff0000" },
    shirt: { image: "shirt1.png", color: "#0000ff" },
    hair: { image: "hair1.png", color: "#000000" },
    accessories: { image: "acc1.png", color: "#ffff00" },
    skin: { image: "skin1.png", color: "#ffddcc" },
  },
};

vi.mock("../../context/UserContext", () => ({
  useUser: () => ({ user: mockUser }),
}));

// Mock del servicio de challenges
vi.mock("../../services/challengeService");

const mockExercise = {
  id: 1,
  question: "¿2 + 2 = 4?",
  answer: "VERDADERO",
  type: "OPEN" as const,
  options: null,
  solution: "Sí, es correcto",
  difficulty: "EASY" as const,
};

const renderChallengePlay = () => {
  return render(
    <MemoryRouter initialEntries={["/challenges/1/play/1/1/CHALLENGER"]}>
      <Routes>
        <Route
          path="/challenges/:challengeId/play/:roundNumber/:questionsPerRound/:rol"
          element={<ChallengePlayPage />}
        />
      </Routes>
    </MemoryRouter>
  );
};

describe("ChallengePlayPage", () => {
  beforeEach(() => {
    vi.clearAllMocks();

    // Configurar mocks
    vi.mocked(challengeService.getAllCategories).mockResolvedValue([
      "MATRICES",
      "DETERMINANTS",
      "SYSTEM_OF_EQUATIONS",
    ]);

    vi.mocked(challengeService.getRandomCategory).mockResolvedValue("MATRICES");

    vi.mocked(challengeService.getRandomExercises).mockResolvedValue([
      mockExercise,
    ]);

    vi.mocked(challengeService.answerRound).mockResolvedValue({
      id: 1,
      challengerId: 1,
      challengedId: 2,
      status: "COMPLETED",
      result: "CHALLENGER_WIN",
      rounds: [],
      maxRounds: 3,
      questionsPerRound: 5,
      creationDate: new Date().toISOString(),
      expireDate: new Date(Date.now() + 86400000).toISOString(),
    });
  });

  describe("Renderizado inicial", () => {
    it("debe renderizar la ruleta y permitir girarla", async () => {
      renderChallengePlay();

      // Esperar a que cargue la ruleta
      const spinButton = await screen.findByRole(
        "button",
        { name: /¡GIRAR!/i },
        { timeout: 10000 }
      );
      expect(spinButton).toBeInTheDocument();

      // Verificar que las categorías están en la ruleta
      expect(screen.getByText(/Matrices/i)).toBeInTheDocument();
      expect(screen.getByText(/Determinantes/i)).toBeInTheDocument();
      expect(screen.getByText(/Sistemas de ecuaciones/i)).toBeInTheDocument();
    }, 15000);

    it("debe llamar a getRandomCategory cuando se hace clic en girar", async () => {
      const user = userEvent.setup();
      renderChallengePlay();

      const spinButton = await screen.findByRole(
        "button",
        { name: /¡GIRAR!/i },
        { timeout: 10000 }
      );
      await user.click(spinButton);

      // Verificar que se llamó al servicio
      expect(challengeService.getRandomCategory).toHaveBeenCalled();
    }, 15000);

    it("debe cargar las categorías al iniciar", async () => {
      renderChallengePlay();

      await screen.findByRole(
        "button",
        { name: /¡GIRAR!/i },
        { timeout: 10000 }
      );

      // Verificar que se llamó a getAllCategories
      expect(challengeService.getAllCategories).toHaveBeenCalled();
    }, 15000);
  });

  describe("Responder preguntas", () => {
    it("debe mostrar feedback positivo cuando se responde correctamente", async () => {
      const user = userEvent.setup();
      renderChallengePlay();

      // Esperar el botón de girar
      const spinButton = await screen.findByRole(
        "button",
        { name: /¡GIRAR!/i },
        { timeout: 10000 }
      );

      // Hacer clic en girar
      await user.click(spinButton);

      // Esperar a que termine la animación de la ruleta (5 segundos)
      await new Promise((resolve) => setTimeout(resolve, 5000));

      // Esperar a que aparezca el popup de categoría
      const categoryPopup = await screen.findByText(
        /Categoría|MATRICES/i,
        {},
        { timeout: 5000 }
      );
      expect(categoryPopup).toBeInTheDocument();

      // Buscar el botón "Comenzar" y hacer clic
      const startButton = await screen.findByRole(
        "button",
        { name: /Comenzar/i },
        { timeout: 5000 }
      );
      await user.click(startButton);

      // Esperar a que aparezca la pregunta
      const question = await screen.findByText(
        /¿2 \+ 2 = 4\?/i,
        {},
        { timeout: 10000 }
      );
      expect(question).toBeInTheDocument();

      // Buscar y hacer clic en el botón VERDADERO
      const trueButton = await screen.findByRole(
        "button",
        { name: /VERDADERO/i },
        { timeout: 5000 }
      );
      await user.click(trueButton);

      // Esperar el feedback de respuesta correcta
      const correctFeedback = await screen.findByText(
        /¡Correcto!/i,
        {},
        { timeout: 10000 }
      );
      expect(correctFeedback).toBeInTheDocument();
    }, 60000);

    it("debe mostrar feedback negativo cuando se responde incorrectamente", async () => {
      const user = userEvent.setup();
      renderChallengePlay();

      // Esperar el botón de girar
      const spinButton = await screen.findByRole(
        "button",
        { name: /¡GIRAR!/i },
        { timeout: 10000 }
      );

      // Hacer clic en girar
      await user.click(spinButton);

      // Esperar a que termine la animación de la ruleta (5 segundos)
      await new Promise((resolve) => setTimeout(resolve, 5000));

      // Esperar a que aparezca el popup de categoría
      const categoryPopup = await screen.findByText(
        /Categoría|MATRICES/i,
        {},
        { timeout: 5000 }
      );
      expect(categoryPopup).toBeInTheDocument();

      // Buscar el botón "Comenzar" y hacer clic
      const startButton = await screen.findByRole(
        "button",
        { name: /Comenzar/i },
        { timeout: 5000 }
      );
      await user.click(startButton);

      // Esperar a que aparezca la pregunta
      const question = await screen.findByText(
        /¿2 \+ 2 = 4\?/i,
        {},
        { timeout: 10000 }
      );
      expect(question).toBeInTheDocument();

      // Buscar y hacer clic en el botón FALSO (respuesta incorrecta)
      const falseButton = await screen.findByRole(
        "button",
        { name: /FALSO/i },
        { timeout: 5000 }
      );
      await user.click(falseButton);

      // Esperar el feedback de respuesta incorrecta
      const incorrectFeedback = await screen.findByText(
        /Incorrecto/i,
        {},
        { timeout: 10000 }
      );
      expect(incorrectFeedback).toBeInTheDocument();
    }, 60000);

    it("debe mostrar timeout cuando se acaba el tiempo sin responder", async () => {
      const user = userEvent.setup();
      renderChallengePlay();

      // Esperar el botón de girar
      const spinButton = await screen.findByRole(
        "button",
        { name: /¡GIRAR!/i },
        { timeout: 10000 }
      );

      // Hacer clic en girar
      await user.click(spinButton);

      // Esperar a que termine la animación de la ruleta (5 segundos)
      await new Promise((resolve) => setTimeout(resolve, 5000));

      // Esperar a que aparezca el popup de categoría
      const categoryPopup = await screen.findByText(
        /Categoría|MATRICES/i,
        {},
        { timeout: 5000 }
      );
      expect(categoryPopup).toBeInTheDocument();

      // Buscar el botón "Comenzar" y hacer clic
      const startButton = await screen.findByRole(
        "button",
        { name: /Comenzar/i },
        { timeout: 5000 }
      );
      await user.click(startButton);

      // Esperar a que aparezca la pregunta
      const question = await screen.findByText(
        /¿2 \+ 2 = 4\?/i,
        {},
        { timeout: 10000 }
      );
      expect(question).toBeInTheDocument();

      // NO hacer clic en ningún botón, simplemente esperar el timeout
      // El componente tiene un timer de 25 segundos, así que esperamos 26 segundos
      const timeoutMessage = await screen.findByText(
        /Se acabó el tiempo|Tiempo agotado|Time's up/i,
        {},
        { timeout: 30000 }
      );
      expect(timeoutMessage).toBeInTheDocument();
    }, 60000);
  });
});
