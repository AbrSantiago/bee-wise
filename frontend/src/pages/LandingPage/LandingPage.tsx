import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import "./LandingPage.css";

export default function LandingPage() {
  const { login } = useAuth();
  const [isLogin, setIsLogin] = useState(true);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    try {
      const url = isLogin
        ? "http://localhost:8080/login"
        : "http://localhost:8080/register";

      const res = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) throw new Error("Error en credenciales o registro");

      const data = await res.json();
      if (isLogin) {
        login(data.token);
      } else {
        alert("Usuario registrado con éxito, ahora podés iniciar sesión.");
        setIsLogin(true);
      }
    } catch (err: any) {
      setError(err.message);
    }
  };

  return (
    <div className="landing-container">
      <div className="form-box">
        <h1 className="title">{isLogin ? "Login" : "Register"}</h1>

        {error && <p className="error">{error}</p>}

        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Usuario"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit">
            {isLogin ? "Iniciar sesión" : "Registrarse"}
          </button>
        </form>

        <p className="toggle">
          {isLogin ? "¿No tenés cuenta?" : "¿Ya tenés cuenta?"}{" "}
          <span onClick={() => setIsLogin(!isLogin)}>
            {isLogin ? "Registrate" : "Iniciá sesión"}
          </span>
        </p>
      </div>
    </div>
  );
}
