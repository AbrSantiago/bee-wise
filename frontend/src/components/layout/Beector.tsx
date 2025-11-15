import "./Beector.css";

interface Props {
  imgSrc?: string;
  size?: number;
}

export default function Beector({
  imgSrc = "/image/BeeChill",
  size = 150,
}: Props) {
  const style = {
    height: `${size}px`,
  };

  return (
    <img
      className="beector-float"
      src={imgSrc}
      alt="Bee reading"
      style={style}
    />
  );
}
