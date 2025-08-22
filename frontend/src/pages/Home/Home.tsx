// src/pages/Home/Home.tsx
import { useState } from 'react'
import MainLayout from '../../components/layout/MainLayout'
import './Home.css'

function Home() {
  const [count, setCount] = useState(0)

  return (
    <MainLayout title="Learn Matrices" sectionInfo="SECTION 1, UNIT 1">
      <div className="home-content">
        <h1>Bienvenido a Bee-Wise</h1>
        <p>Aprende matemáticas y matrices paso a paso</p>

        <div className="lesson-path">
          <div className="lesson-node active">
            <span className="lesson-icon">⭐</span>
            <span className="lesson-title">Matrices Básicas</span>
          </div>
          
          <div className="lesson-node">
            <span className="lesson-icon">🔒</span>
            <span className="lesson-title">Operaciones con Matrices</span>
          </div>
          
          <div className="lesson-node">
            <span className="lesson-icon">🔒</span>
            <span className="lesson-title">Multiplicación de Matrices</span>
          </div>
        </div>
        
        <button className="start-lesson-btn" onClick={() => setCount((count) => count + 1)}>
          INICIAR LECCIÓN {count > 0 && `(${count})`}
        </button>
      </div>
    </MainLayout>
  )
}

export default Home;