import { useNavigate } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout";
import "./FeatureLocked.css";
import lockedBeeImage from "/image/BeeAngry.png";

export function FeatureLockedPage() {
  const navigate = useNavigate();

  return (
    <MainLayout title="Desafíos">
      <div className="feature-locked-container">
        <div className="locked-card">
          <img
            src={lockedBeeImage}
            alt="Bee locked"
            className="locked-bee-image"
          />
          <h1>¡Desafíos Bloqueados!</h1>
          <p className="locked-message">
            Necesitas alcanzar el <strong>Nivel 2</strong> para desbloquear los
            desafíos.
          </p>
          <p className="locked-hint">
            ¡Seguí completando lecciones para subir de nivel!
          </p>
          <button className="back-button" onClick={() => navigate("/home")}>
            Volver a Lecciones
          </button>
        </div>
      </div>
    </MainLayout>
  );
}
