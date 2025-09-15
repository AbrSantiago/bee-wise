import { useState } from "react";
import "./RegisterPage.css";
import userService from "../../services/userService";
import ValidatedInput from "../../components/layout/ValidateInput";

export default function RegisterPage() {
  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [success, setSuccess] = useState("");

  // --- Validadores individuales ---
  const validateName = (value: string) => {
    if (!value.trim()) return "El nombre es obligatorio";
    if (value.length > 50) return "El nombre no puede superar 50 caracteres";
    return "";
  };

  const validateSurname = (value: string) => {
    if (!value.trim()) return "El apellido es obligatorio";
    if (value.length > 50) return "El apellido no puede superar 50 caracteres";
    return "";
  };

  const validateEmail = (value: string) => {
    if (!value.trim()) return "El email es obligatorio";
    if (value.length > 150) return "El email no puede superar 150 caracteres";
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(value)) return "El email no tiene un formato válido";
    return "";
  };

  const validateUsername = (value: string) => {
    if (!value.trim()) return "El usuario es obligatorio";
    if (value.length > 30) return "El usuario no puede superar 30 caracteres";
    return "";
  };

  const validatePassword = (value: string) => {
    if (!value.trim()) return "La contraseña es obligatoria";
    if (value.length < 8) return "La contraseña debe tener al menos 8 caracteres";
    if (value.length > 255) return "La contraseña es demasiado larga";
    return "";
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccess("");

    if (
      validateName(name) ||
      validateSurname(surname) ||
      validateEmail(email) ||
      validateUsername(username) ||
      validatePassword(password)
    ) {
      return;
    }

    try {
      await userService.register({
        name,
        surname,
        email,
        username,
        password,
      });

      setSuccess("Usuario registrado con éxito. Ahora podés iniciar sesión.");
      setName("");
      setSurname("");
      setEmail("");
      setUsername("");
      setPassword("");
    } catch (err: any) {
      // Podrías mapear error del backend al campo correspondiente
      console.error(err);
    }
  };

  return (
    <div className="register-container">
      <form onSubmit={handleSubmit} className="register-box">
        <h1>Registro</h1>

        {success && <p className="success">{success}</p>}

        <ValidatedInput
          type="text"
          placeholder="Nombre"
          value={name}
          setValue={setName}
          validate={validateName}
          maxLength={50}
        />

        <ValidatedInput
          type="text"
          placeholder="Apellido"
          value={surname}
          setValue={setSurname}
          validate={validateSurname}
          maxLength={50}
        />

        <ValidatedInput
          type="email"
          placeholder="Email"
          value={email}
          setValue={setEmail}
          validate={validateEmail}
          maxLength={150}
        />

        <ValidatedInput
          type="text"
          placeholder="Usuario"
          value={username}
          setValue={setUsername}
          validate={validateUsername}
          maxLength={30}
        />

        <ValidatedInput
          type="password"
          placeholder="Contraseña (mínimo 8 caracteres)"
          value={password}
          setValue={setPassword}
          validate={validatePassword}
        />

        <button type="submit">Registrarse</button>
      </form>
    </div>
  );
}
