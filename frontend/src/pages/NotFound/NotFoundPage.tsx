import { Navigate } from "react-router-dom";

export function NotFoundPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
      <h1 style={{ fontSize: '4rem', color: '#2563eb' }}>404</h1>
      <h2 style={{ fontSize: '2rem', color: '#374151' }}>Page Not Found</h2>
      <p style={{ color: '#6b7280', marginBottom: '2rem' }}>
        Sorry, the page you are looking for does not exist.
      </p>
      {/* <Navigate to="/" replace /> */}
    </div>
  );
}
