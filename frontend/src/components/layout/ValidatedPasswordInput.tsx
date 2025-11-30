import { useState } from "react";
import "./ValidatedPasswordInput.css";

interface ValidatedPasswordInputProps {
  password: string;
  setPassword: (val: string) => void;
  confirmPassword: string;
  setConfirmPassword: (val: string) => void;
}

export default function ValidatedPasswordInput({
  password,
  setPassword,
  confirmPassword,
  setConfirmPassword,
}: ValidatedPasswordInputProps) {
  const [showPassword, setShowPassword] = useState(false);
  const [passwordError, setPasswordError] = useState("");
  const [confirmError, setConfirmError] = useState("");

  // Requisitos básicos
  const validatePassword = (value: string) => {
    if (!value) return "La contraseña es obligatoria";
    if (value.length < 8) return "Debe tener al menos 8 caracteres";
    if (!/[A-Z]/.test(value)) return "Debe contener una mayúscula";
    if (!/[0-9]/.test(value)) return "Debe contener un número";
    if (!/[!@#$%^&*]/.test(value)) return "Debe contener un símbolo";
    return "";
  };

  const validateConfirm = (value: string) => {
    if (!value) return "Debe confirmar la contraseña";
    if (value !== password) return "Las contraseñas no coinciden";
    return "";
  };

  const getStrength = (value: string) => {
    if (value.length < 8) return "Débil";
    let score = 0;
    if (/[A-Z]/.test(value)) score++;
    if (/[0-9]/.test(value)) score++;
    if (/[!@#$%^&*]/.test(value)) score++;
    if (value.length >= 12) score++;

    if (score <= 1) return "Débil";
    if (score === 2) return "Media";
    return "Fuerte";
  };

  return (
    <div className="password-container">
      {/* Campo contraseña */}
      <div className="input-group password-input">
        <input
          type={showPassword ? "text" : "password"}
          placeholder="Contraseña"
          value={password}
          onChange={(e) => {
            const val = e.target.value;
            setPassword(val);
            setPasswordError(validatePassword(val));
          }}
          onBlur={() => setPasswordError(validatePassword(password))}
          className={passwordError ? "input-error" : ""}
        />
        <span
          className="toggle-visibility"
          onClick={() => setShowPassword(!showPassword)}
        >
          {showPassword ? "🙈" : "👁️"}
        </span>
        {passwordError && <p className="error">{passwordError}</p>}

        {/* Indicador de fortaleza */}
        {password && !passwordError && (
          <p className={`strength ${getStrength(password).toLowerCase()}`}>
            Fortaleza: {getStrength(password)}
          </p>
        )}

        {/* Tooltip con requisitos */}
        <div className="tooltip">
          ℹ️
          <span className="tooltip-text">
            La contraseña debe tener:
            <br />• Al menos 8 caracteres
            <br />• Una mayúscula
            <br />• Un número
            <br />• Un símbolo (!@#$%^&*)
          </span>
        </div>
      </div>

      {/* Campo confirmar contraseña */}
      <div className="input-group">
        <input
          type="password"
          placeholder="Confirmar contraseña"
          value={confirmPassword}
          onChange={(e) => {
            const val = e.target.value;
            setConfirmPassword(val);
            setConfirmError(validateConfirm(val));
          }}
          onBlur={() => setConfirmError(validateConfirm(confirmPassword))}
          className={confirmError ? "input-error" : ""}
        />
        {confirmError && <p className="error">{confirmError}</p>}
      </div>
    </div>
  );
}
