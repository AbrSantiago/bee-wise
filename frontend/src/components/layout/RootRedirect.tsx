import { Navigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function RootRedirect() {
  const { accessToken } = useAuth();

  if (accessToken) {
    return <Navigate to="/home" replace />;
  }

  return <Navigate to="/landing" replace />;
}