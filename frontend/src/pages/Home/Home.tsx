// src/pages/Home/Home.tsx
import { useState } from 'react'
import MainLayout from '../../components/layout/MainLayout'
import './Home.css'
import { Link } from 'react-router-dom';

function Home() {

  return (
    <MainLayout title="Lessons Page">
      {/* <h1 className="lesson-title">Lessons Page</h1> */}
      <div className="lesson-btn-list">
        <div className="lesson-btn-container">
          <span className="lesson-btn-label">Lección 1 - Matrices básicas</span>
          <Link to="/practice">
            <button className="lesson-btn">
              <span className="star">★</span>
            </button>
          </Link>
        </div>

        <div className="lesson-btn-container">
          <span className="lesson-btn-label">Ejercicio 2</span>
          <Link to="/drag-and-drop">
            <button className="lesson-btn">
              <span className="star">★</span>
            </button>
          </Link>
        </div>
        <div className="lesson-btn-container">
          <span className="lesson-btn-label">Ejercicio 3</span>
          <Link to="/drag-and-drop">
            <button className="lesson-btn">
              <span className="star">★</span>
            </button>
          </Link>
        </div>
        <div className="lesson-btn-container">
          <span className="lesson-btn-label">Ejercicio 4</span>
          <Link to="/drag-and-drop">
            <button className="lesson-btn">
              <span className="star">★</span>
            </button>
          </Link>
        </div>
      </div>
    </MainLayout>
  );
}

export default Home;