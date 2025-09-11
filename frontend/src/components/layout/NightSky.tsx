import { useMemo } from "react";
import "./NightSky.css";

type Star = { left: string; top: string; size: number; delay: number; duration: number; opacity: number };

function random(min:number,max:number){ return Math.random()*(max-min)+min; }

export default function NightSky({ count = 120 }: { count?: number }) {
  // genera posiciones solo en mount
  const stars: Star[] = useMemo(() => {
    return Array.from({length: count}).map(() => ({
      left: `${random(0,100).toFixed(2)}%`,
      top: `${random(0,85).toFixed(2)}%`,
      size: Math.round(random(1,3)),
      delay: parseFloat(random(0,8).toFixed(2)),
      duration: parseFloat(random(3,9).toFixed(2)),
      opacity: parseFloat(random(0.6,1).toFixed(2)),
    }));
  }, [count]);

  return (
    <div className="night-sky">
      <div className="stars-layer">
        {stars.map((s, i) => (
          <span
            className="star"
            key={i}
            style={{
              left: s.left,
              top: s.top,
              width: `${s.size}px`,
              height: `${s.size}px`,
              animationDuration: `${s.duration}s`,
              animationDelay: `${s.delay}s`,
              opacity: s.opacity,
            }}
          />
        ))}
      </div>

      <div className="shooting" aria-hidden />
      {/* tu contenido de UI */}
      <div className="home-ui"> ... </div>
    </div>
  );
}
