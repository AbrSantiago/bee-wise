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
              <Route path="/challenges" element={<ChallengesPage />} />
              <Route path="/ranking" element={<RankingPage />} />
              <Route path="/test" element={<Test />} />
            </Route>

            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </UserProvider>
      </UserPointsProvider>
    </AuthProvider>
  );
}

export default App;
