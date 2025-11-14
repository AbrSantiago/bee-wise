import { Navigate } from "react-router-dom";
import { useUser } from "../../context/UserContext";

type FeatureProtectedRouteProps = {
  children: React.ReactNode;
  requiredLevel: number;
  redirectTo?: string;
};

export function FeatureProtectedRoute({
  children,
  requiredLevel,
  redirectTo = "/feature-locked",
}: FeatureProtectedRouteProps) {
  const { user } = useUser();

  if (!user?.level?.level || user.level.level < requiredLevel) {
    return <Navigate to={redirectTo} replace />;
  }

  return <>{children}</>;
}
