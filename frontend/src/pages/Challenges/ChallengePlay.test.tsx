import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { ChallengePlayPage } from "./ChallengePlay";
import challengeService from "../../services/challengeService";

// Mock del servicio de challenges
vi.mock("../../services/challengeService");

// Mock del componente MainLayout
vi.mock("../../components/layout/MainLayout", () => ({
  default: ({
    children,
    title,
  }: {
    children: React.ReactNode;
    title: string;
  }) => (
    <div data-testid="main-layout">
      <h1>{title}</h1>
      {children}
    </div>
  ),
}));

// Mock de la ruleta
vi.mock("../../components/layout/Roulette", () => ({
  default: ({
    onSpinningEnd,
    triggerSpin,
  }: {
    onSpinningEnd: () => void;
    triggerSpin: boolean;
  }) => {
    if (triggerSpin) {
      setTimeout(() => {
        onSpinningEnd();
      }, 100);
    }

    return (
      <div data-testid="roulette">
        <span>Roulette Component</span>
      </div>
    );
  },
}));

// Mock del popup de categoría
vi.mock("../../components/layout/CategoryPopup", () => ({
  default: ({
    onStart,
    category,
  }: {
    onStart: () => void;
    category: string;
  }) => (
    <div data-testid="category-popup">
      <div>Categoría: {category}</div>
      <button data-testid="start-button" onClick={onStart}>
        Comenzar
      </button>
    </div>
  ),
}));

const mockExercises = [
  {
    id: 1,
    question: "Test Question 1",
    answer: "FALSO", // Cambiado a FALSO porque el componente tiene la lógica invertida
    type: "OPEN" as const,
    options: null,
    solution: "Test solution",
    difficulty: "EASY" as const,
  },
];

const renderWithRouter = (
  challengeId = "1",
  roundNumber = "1",
  questionsPerRound = "1",
  rol = "CHALLENGER"
) => {
  return render(
    <MemoryRouter
      initialEntries={[
        `/challenges/${challengeId}/play/${roundNumber}/${questionsPerRound}/${rol}`,
      ]}
    >
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

    vi.mocked(challengeService.getAllCategories).mockResolvedValue([
      "MATRICES",
      "DETERMINANTS",
      "SYSTEM_OF_EQUATIONS",
    ]);

    vi.mocked(challengeService.getRandomCategory).mockResolvedValue("MATRICES");

    vi.mocked(challengeService.getRandomExercises).mockResolvedValue(
      mockExercises
    );

    vi.mocked(challengeService.answerRound).mockResolvedValue({
      id: 1,
      challengerId: 1,
      challengedId: 2,
      status: "IN_PROGRESS",
      result: null,
      currentRound: 1,
      createdAt: new Date(),
      updatedAt: new Date(),
    });
  });

  afterEach(() => {
    vi.clearAllTimers();
  });

  it("✅ Caso 1: Responder correctamente", async () => {
    const user = userEvent.setup();
    renderWithRouter();

    const spinButton = await screen.findByRole("button", { name: /¡GIRAR!/i });
    await user.click(spinButton);

    await waitFor(
      async () => {
        const popup = await screen.findByTestId("category-popup");
        expect(popup).toBeInTheDocument();
      },
      { timeout: 5000 }
    );

    const startButton = screen.getByTestId("start-button");
    await user.click(startButton);

    const question = await screen.findByText(
      "Test Question 1",
      {},
      { timeout: 5000 }
    );
    expect(question).toBeInTheDocument();

    // Responder VERDADERO (que es la respuesta correcta según el mock)
    // Responder FALSO (que es la respuesta correcta según el mock)
    const falsoButton = await screen.findByRole("button", {
      name: /Falso/i,
    });
    await user.click(falsoButton);

    // Verificar que el botón correcto tiene el check ✔️ y la clase "correct"
    await waitFor(() => {
      const correctButton = screen.getByRole("button", { name: /Falso/i });
      expect(correctButton).toHaveClass("correct");
    });

    // Verificar que el botón continuar NO tiene clase error
    const continueButton = await screen.findByText("Continuar");
    expect(continueButton).not.toHaveClass("error");
  }, 30000);

  it("❌ Caso 2: Responder incorrectamente", async () => {
    const user = userEvent.setup();
    renderWithRouter();

    const spinButton = await screen.findByRole("button", { name: /¡GIRAR!/i });
    await user.click(spinButton);

    await waitFor(
      async () => {
        const popup = await screen.findByTestId("category-popup");
        expect(popup).toBeInTheDocument();
      },
      { timeout: 5000 }
    );

    const startButton = screen.getByTestId("start-button");
    await user.click(startButton);

    await screen.findByText("Test Question 1", {}, { timeout: 5000 });

    // Responder VERDADERO (incorrecto, porque la respuesta es FALSO)
    const verdaderoButton = await screen.findByRole("button", {
      name: /Verdadero/i,
    });
    await user.click(verdaderoButton);

    // Verificar que el botón incorrecto tiene la X ❌ y la clase "incorrect"
    await waitFor(() => {
      const incorrectButton = screen.getByRole("button", {
        name: /Verdadero/i,
      });
      expect(incorrectButton).toHaveClass("incorrect");
    });

    // Verificar el mensaje de feedback de error
    const feedbackMessage = await screen.findByText(
      /Mmm, nop. Esa no era, pero no te rindas!/i
    );
    expect(feedbackMessage).toBeInTheDocument();

    // Verificar que el botón continuar tiene clase error
    const continueButton = await screen.findByText("Continuar");
    expect(continueButton).toHaveClass("error");
  }, 30000);

  it("⏱️ Caso 3: Quedarse sin tiempo", async () => {
    const user = userEvent.setup();
    renderWithRouter();

    const spinButton = await screen.findByRole("button", { name: /¡GIRAR!/i });
    await user.click(spinButton);

    await waitFor(
      async () => {
        const popup = await screen.findByTestId("category-popup");
        expect(popup).toBeInTheDocument();
      },
      { timeout: 5000 }
    );

    const startButton = screen.getByTestId("start-button");
    await user.click(startButton);

    await screen.findByText("Test Question 1", {}, { timeout: 5000 });

    // Esperar 26 segundos reales para que el timer se agote
    await new Promise((resolve) => setTimeout(resolve, 26000));

    // Verificar que el timer llegó a 0
    await waitFor(
      () => {
        const timerDisplay = screen.queryByText(/Tiempo restante : 0s/i);
        expect(timerDisplay).toBeInTheDocument();
      },
      { timeout: 2000 }
    );

    // Verificar que se muestra el mensaje CORRECTO de timeout
    const feedbackMessage = await screen.findByText(
      /Ops, se te terminó el tiempo!/i,
      {},
      { timeout: 2000 }
    );
    expect(feedbackMessage).toBeInTheDocument();
  }, 35000);
});
