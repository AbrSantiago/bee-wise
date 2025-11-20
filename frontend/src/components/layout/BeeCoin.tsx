import React from "react";

interface BeeCoinProps {
  size?: number | string;
}

const BeeCoin: React.FC<BeeCoinProps> = ({ size = 40 }) => {
  return (
    <img
      src="/image/BeeCoin.png"
      alt="BeeCoin"
      className="bee-coin-img"
      style={{ maxWidth: size, maxHeight: size }}
    />
  );
};

export default BeeCoin;
