// src/App.tsx
import { Route, Routes } from 'react-router-dom'
import Home from './pages/Home/Home'
import { LessonsPage } from './pages/Lessons/Lessons.tsx'
import { PracticePage } from './pages/Practice/Practice.tsx'
import { ProfilePage } from './pages/Profile/Profile.tsx'
import { NotFoundPage } from './pages/NotFound/NotFoundPage.tsx'
import Test from './pages/Test'
import Example from './components/DragAndDrop/dragAndDrop.tsx'

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/lessons" element={<LessonsPage />} />
        <Route path="/practice" element={<PracticePage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/test" element={<Test />} />
        <Route path="/drag-and-drop" element={<Example />} />
        <Route path="*" element={<NotFoundPage />} />
        {/* Puedes agregar más rutas según sea necesario */}
        {/* Add more routes as needed */}
      </Routes> 
    </div>
  )
}

export default App