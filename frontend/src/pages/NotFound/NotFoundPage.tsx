import React from "react";

export function NotFoundPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
      <h1 style={{ fontSize: '4rem', color: '#2563eb' }}>404</h1>
      <h2 style={{ fontSize: '2rem', color: '#374151' }}>Page Not Found</h2>
      <p style={{ color: '#6b7280', marginBottom: '2rem' }}>
        Sorry, the page you are looking for does not exist.
      </p>
      <a href="/" style={{ padding: '0.75rem 1.5rem', background: '#2563eb', color: 'white', borderRadius: '0.5rem', textDecoration: 'none' }}>
        Go Home
      </a>
    </div>
  );
}
