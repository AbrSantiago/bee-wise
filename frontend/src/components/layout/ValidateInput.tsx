import { useState } from "react";

interface ValidatedInputProps {
  type: string;
  placeholder: string;
  value: string;
  setValue: (val: string) => void;
  validate: (val: string) => string;
  maxLength?: number;
  required?: boolean;
}

export default function ValidatedInput({
  type,
  placeholder,
  value,
  setValue,
  validate,
  maxLength,
  required = true,
}: ValidatedInputProps) {
  const [error, setError] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const val = e.target.value;
    setValue(val);
    setError(validate(val));
  };

  const handleBlur = () => {
    setError(validate(value));
  };

  return (
    <div className="input-group">
      <input
        type={type}
        placeholder={placeholder}
        value={value}
        maxLength={maxLength}
        onChange={handleChange}
        onBlur={handleBlur}
        required={required}
      />
      {error && <p className="error">{error}</p>}
    </div>
  );
}
