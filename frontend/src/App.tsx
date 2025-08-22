// src/App.tsx
import { Route, Routes } from 'react-router-dom'
import Home from './pages/Home/Home'
import { LessonsPage } from './pages/Lessons/Lessons.tsx'
import { PracticePage } from './pages/Practice/Practice.tsx'
import { ProfilePage } from './pages/Profile/Profile.tsx'

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/lessons" element={<LessonsPage />} />
        <Route path="/practice" element={<PracticePage />} />
        <Route path="/profile" element={<ProfilePage />} />
        {/* Add more routes as needed */}
      </Routes> 
    </div>
  )
}

export default App