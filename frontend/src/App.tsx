// src/App.tsx
import { Route, Routes } from 'react-router-dom'
import Home from './pages/Home/Home'
import { PracticePage } from './pages/Practice/Practice.tsx'
import { ProfilePage } from './pages/Profile/Profile.tsx'
import { NotFoundPage } from './pages/NotFound/NotFoundPage.tsx'
import Test from './pages/Test'
import PracticeDND from './pages/Practice/PracticeDND.tsx'
import { RankingPage } from './pages/Ranking/Ranking.tsx'
import { ChallengesPage } from './pages/Challenges/Challenges.tsx'

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/practice/:id" element={<PracticePage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/challenges" element={<ChallengesPage />} />
        <Route path="/ranking" element={<RankingPage />} />
        <Route path="/test" element={<Test />} />
        <Route path="/drag-and-drop" element={<PracticeDND />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes> 
    </div>
  )
}

export default App