import { useState } from "react";
import "./RegisterPage.css";
import userService from "../../services/userService";
import ValidatedInput from "../../components/layout/ValidateInput";
import ValidatedPasswordInput from "../../components/layout/ValidatedPasswordInput";

export default function RegisterPage() {
  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [success, setSuccess] = useState("");
  const [generalError, setGeneralError] = useState("");
  const [usernameError, setUsernameError] = useState("");
  const [emailError, setEmailError] = useState("");

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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccess("");
    setGeneralError("");
    setUsernameError("");
    setEmailError("");

    // Validaciones mínimas antes de enviar
    if (
      validateName(name) ||
      validateSurname(surname) ||
      validateEmail(email) ||
      validateUsername(username)
    ) {
      return;
    }

    if (password !== confirmPassword) {
      setGeneralError("Las contraseñas no coinciden");
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
      setConfirmPassword("");
    } catch (err: any) {
      const data = err?.response?.data;

      if (data?.error) {
        if (data.error.includes("Username")) {
          setUsernameError("El nombre de usuario ya existe");
        } else if (data.error.includes("Email")) {
          setEmailError("El email ya está en uso");
        } else {
          setGeneralError(data.error);
        }
      } else {
        setGeneralError("Error al registrar usuario");
      }
    }
  };

  return (
    <div className="register-container">
      <form onSubmit={handleSubmit} className="register-box">
        <h1>Registro</h1>

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
          externalError={emailError}
        />

        <ValidatedInput
          type="text"
          placeholder="Usuario"
          value={username}
          setValue={setUsername}
          validate={validateUsername}
          maxLength={30}
          externalError={usernameError}
        />

        <ValidatedPasswordInput
          password={password}
          setPassword={setPassword}
          confirmPassword={confirmPassword}
          setConfirmPassword={setConfirmPassword}
        />

        {success && <p className="success">{success}</p>}
        {generalError && <p className="error">{generalError}</p>}

        <button type="submit">Registrarse</button>
      </form>
    </div>
  );
}
