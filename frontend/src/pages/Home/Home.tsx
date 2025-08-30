// src/pages/Home/Home.tsx
import { useState } from 'react'
import MainLayout from '../../components/layout/MainLayout'
import './Home.css'
import { Link } from 'react-router-dom';

function Home() {

  return (
    <MainLayout title="Learn Matrices" sectionInfo="SECTION 1, UNIT 1">
      <div className="home-content">
        {/* <h1>Bienvenido a Bee-Wise</h1> */}
        {/* <p>Aprende matemáticas y matrices paso a paso</p> */}

        <div className="lesson-path">
          <Link to="/lessons">
            <div className="lesson-node active">
              <span className="lesson-icon">⭐</span>
              <span className="lessons-title">Matrices Básicas</span>
            </div>
          </Link>
          
          <div className="lesson-node">
            <span className="lesson-icon">🔒</span>
            <span className="lessons-title">Operaciones con Matrices</span>
          </div>
          
          <div className="lesson-node">
            <span className="lesson-icon">🔒</span>
            <span className="lessons-title">Operaciones con Matrices</span>
          </div>

          <div className="lesson-node">
            <span className="lesson-icon">🔒</span>
            <span className="lessons-title">Operaciones con Matrices</span>
          </div>

          <div className="lesson-node">
            <span className="lesson-icon">🔒</span>
            <span className="lessons-title">Multiplicación de Matrices</span>
          </div>
        </div>
        
      </div>
    </MainLayout>
  )
}

export default Home;