import { Route, Routes } from "react-router-dom";
import Home from "./pages/Home/Home";
import { PracticePage } from "./pages/Practice/Practice";
import { ProfilePage } from "./pages/Profile/Profile";
import { NotFoundPage } from "./pages/NotFound/NotFoundPage";
import Test from "./pages/Test";
import { RankingPage } from "./pages/Ranking/Ranking";
import { ChallengesPage } from "./pages/Challenges/Challenges";
import { AuthProvider } from "./context/AuthContext";
import { UserPointsProvider } from "./context/UserPointsContext";
import { ProtectedLayout } from "./components/layout/ProtectedLayout";
import LandingPage from "./pages/LandingPage/LandingPage";
import LoginPage from "./pages/Login/LoginPage";
import RegisterPage from "./pages/Register/RegisterPage";
import RootRedirect from "./components/layout/RootRedirect";
import { UserProvider } from "./context/UserContext";
import { ChallengePlayPage } from "./pages/Challenges/ChallengePlay";
import { AvatarEditPage } from "./pages/AvatarPage/AvatarEditPage";
import { ShopPage } from "./pages/Shop/ShopPage";
import { FeatureLockedPage } from "./pages/FeatureLocked/FeatureLockedPage";
import { FeatureProtectedRoute } from "./components/layout/FeatureProtectedRoute";

function App() {
  return (
    <AuthProvider>
      <UserPointsProvider>
        <UserProvider>
          <Routes>
            <Route path="/" element={<RootRedirect />} />

            {/* Landing con login + register */}
            <Route path="/landing" element={<LandingPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            {/* Auth required */}
            <Route element={<ProtectedLayout />}>
              <Route path="/home" element={<Home />} />
              <Route path="/practice/:id" element={<PracticePage />} />
              <Route path="/profile" element={<ProfilePage />} />
              <Route path="/avatar" element={<AvatarEditPage />} />

              {/* Challenges - Requiere Nivel 2 */}
              <Route
                path="/challenges"
                element={
                  <FeatureProtectedRoute requiredLevel={2}>
                    <ChallengesPage />
                  </FeatureProtectedRoute>
                }
              />

              <Route path="/feature-locked" element={<FeatureLockedPage />} />
              <Route path="/ranking" element={<RankingPage />} />
              <Route path="/shop" element={<ShopPage />} />
              <Route path="/test" element={<Test />} />
              <Route
                path="/challenge/:challengeId/round/:roundNumber/:questionsPerRound/:rol"
                element={<ChallengePlayPage />}
              />
            </Route>

            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </UserProvider>
      </UserPointsProvider>
    </AuthProvider>
  );
}

export default App;
