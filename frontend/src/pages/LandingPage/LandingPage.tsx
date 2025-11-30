import { Link } from "react-router-dom";
import "./LandingPage.css";

export default function LandingPage() {
  return (
    <div className="landing-container">
      <div className="landing">
        <h1 className="title">Bienvenido</h1>
        <p>Accedé con tu cuenta o registrate para empezar</p>
        <div className="buttons">
          <Link to="/login" className="btn">Iniciar sesión</Link>
          <Link to="/register" className="btn">Registrarse</Link>
        </div>
      </div>
    </div>
  );
}