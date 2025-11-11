import { useEffect, useRef } from "react";
import "./Confetti.css";

interface ConfettiProps {
  duration?: number;
  intensity?: "low" | "medium" | "high";
}

const Confetti: React.FC<ConfettiProps> = ({
  duration = 5000,
  intensity = "medium",
}) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const intervalRef = useRef<NodeJS.Timeout | null>(null);

  const confettiColors = ["#FCE18A", "#FF726D", "#B48DEF", "#F4306D"];
  const confettiAnimations = ["slow", "medium", "fast"];

  const getInterval = () => {
    switch (intensity) {
      case "low":
        return 40;
      case "medium":
        return 15;
      case "high":
        return 5;
      default:
        return 15;
    }
  };

  useEffect(() => {
    const renderConfetti = () => {
      if (!containerRef.current) return;

      const confettiEl = document.createElement("div");
      const confettiSize = Math.floor(Math.random() * 3) + 7 + "px";
      const confettiBackground =
        confettiColors[Math.floor(Math.random() * confettiColors.length)];
      const confettiLeft = Math.floor(Math.random() * window.innerWidth) + "px";
      const confettiAnimation =
        confettiAnimations[
          Math.floor(Math.random() * confettiAnimations.length)
        ];

      confettiEl.classList.add(
        "confetti",
        "confetti--animation-" + confettiAnimation
      );

      confettiEl.style.left = confettiLeft;
      confettiEl.style.width = confettiSize;
      confettiEl.style.height = confettiSize;
      confettiEl.style.backgroundColor = confettiBackground;

      setTimeout(() => {
        if (confettiEl.parentNode) {
          confettiEl.parentNode.removeChild(confettiEl);
        }
      }, 3000);

      containerRef.current?.appendChild(confettiEl);
    };

    intervalRef.current = setInterval(renderConfetti, getInterval());

    const timeout = setTimeout(() => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
      }
    }, duration);

    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
      }
      clearTimeout(timeout);
    };
  }, [duration, intensity]);

  return <div ref={containerRef} className="confetti-container" />;
};

export default Confetti;
