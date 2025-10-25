import type { ShopItem } from "../../services/userService";
import "./OwnedItemCard.css";

type Props = {
  item: ShopItem;
};

export function OwnedItemCard({ item }: Props) {
  const getImagePath = () => {
    switch (item.category) {
      case "SKIN":
        return `src/assets/avatars/skin/${item.image}`;
      case "HAIR":
        return `src/assets/avatars/hair/${item.image}`;
      case "SHIRT":
        return `src/assets/avatars/shirt/${item.image}`;
      case "BACKGROUND":
        return `src/assets/avatars/bg/${item.image}`;
      default:
        return `src/assets/shop/${item.image}`;
    }
  };

  const getCategoryClass = () => {
    switch (item.category) {
      case "SKIN":
        return "skin-item";
      case "HAIR":
        return "hair-item";
      case "SHIRT":
        return "shirt-item";
      case "BACKGROUND":
        return "background-item";
      default:
        return "";
    }
  };

  return (
    <div className="item-card">
      <img
        src={getImagePath()}
        alt={item.name}
        className={`item-image ${getCategoryClass()}`}
      />
      <p className="item-name">{item.name}</p>
    </div>
  );
}
