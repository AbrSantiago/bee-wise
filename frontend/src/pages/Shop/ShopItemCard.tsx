import type { ShopItem } from "../../services/userService";
import "./ShopItemCard.css";

type Props = {
  item: ShopItem;
  userOwnsItem: (id: number) => boolean;
  purchasingItemId: number | null;
  loading: boolean;
  openConfirmModal: (item: ShopItem) => void;
};

export function ShopItemCard({
  item,
  userOwnsItem,
  purchasingItemId,
  loading,
  openConfirmModal,
}: Props) {
  const isOwned = userOwnsItem(item.id);
  const isPurchasing = purchasingItemId === item.id;

  const getImagePath = () => {
    switch (item.category) {
      case "SKIN":
        return `/avatars/skin/${item.image}`;
      case "HAIR":
        return `avatars/hair/${item.image}`;
      case "SHIRT":
        return `/avatars/shirt/${item.image}`;
      case "BACKGROUND":
        return `/avatars/bg/${item.image}`;
      default:
        return `/shop/${item.image}`;
    }
  };

  const getCategoryClass = () => {
    switch (item.category) {
      case "SKIN":
        return "shop-skin-item";
      case "HAIR":
        return "shop-hair-item";
      case "SHIRT":
        return "shop-shirt-item";
      case "BACKGROUND":
        return "shop-background-item";
      default:
        return "";
    }
  };

  return (
    <button
      className={`shop-item-card ${isOwned ? "owned" : ""}`}
      onClick={() => openConfirmModal(item)}
      disabled={isOwned || (loading && isPurchasing)}
    >
      <img
        src={getImagePath()}
        alt={item.name}
        className={`shop-item-image ${getCategoryClass()}`}
      />
      <div className={`shop-item-separator`} />
      <div className="buy-button">
        {isPurchasing
          ? "Comprando..."
          : isOwned
          ? "Adquirido"
          : `🪙 ${item.price}`}
      </div>
    </button>
  );
}
