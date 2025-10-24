import type { Avatar as AvatarType } from "../../services/userService";
import "./Avatar.css";

type AvatarProps = {
  avatar: AvatarType;
  size?: number; // default 220px
};

export function Avatar({ avatar, size = 220 }: AvatarProps) {
  const style = {
    width: `${size}px`,
    height: `${size}px`,
  };

  return (
    <div className="avatar-preview" style={style}>
      <img
        src={`src/assets/avatars/bg/${avatar.background.image}`}
        alt="background"
        className="avatar-layer background"
      />
      <img
        src={`src/assets/avatars/shirt/${avatar.shirt.image}`}
        alt="shirt"
        className="avatar-layer"
      />
      <img
        src={`src/assets/avatars/skin/${avatar.skin.image}`}
        alt="skin"
        className="avatar-layer"
      />
      <img
        src={`src/assets/avatars/hair/${avatar.hair.image}`}
        alt="hair"
        className="avatar-layer"
      />
    </div>
  );
}
