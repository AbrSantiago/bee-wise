import type { ShopItem, ItemCategory } from "../../services/userService";
import shopService from "../../services/shopService";
import "./OwnedShop.css";
import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import userService from "../../services/userService";

export function OwnedShop() {
  const [shopItems, setShopItems] = useState<ShopItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [purchasingItemId, setPurchasingItemId] = useState<number | null>(null);
  const [selectedCategory, setSelectedCategory] = useState<
    ItemCategory | "ALL"
  >("ALL");
  const [itemsByCategory, setItemsByCategory] = useState<Record<
    ItemCategory,
    ShopItem[]
  > | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [itemToConfirm, setItemToConfirm] = useState<ShopItem | null>(null);
  const [userItems, setUserItems] = useState<ShopItem[]>([]); // 👈 NUEVO: Items del usuario

  const openConfirmModal = (item: ShopItem) => {
    setItemToConfirm(item);
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setItemToConfirm(null);
  };

  const fetchShopItems = async () => {
    try {
      const items = await shopService.getAllItems();
      setShopItems(items);
      console.log("Items de la tienda:", items);
    } catch (error) {
      console.error("Error al traer los items de la tienda:", error);
    }
  };

  const fetchItemsByCategory = async () => {
    try {
      const items = await shopService.getItemsByCategory();
      setItemsByCategory(items);
      console.log("Items por categoría:", items);
    } catch (error) {
      console.error("Error al traer items por categoría:", error);
    }
  };

  const fetchUserItems = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) return;

      const userData = await userService.getCurrentUser(token);
      setUserItems(userData.items || []);
      console.log("Items del usuario:", userData.items);
    } catch (error) {
      console.error("Error al traer items del usuario:", error);
    }
  };

  const userOwnsItem = (itemId: number): boolean => {
    return userItems.some((userItem) => userItem.id === itemId);
  };

  useEffect(() => {
    fetchShopItems();
    fetchItemsByCategory();
    fetchUserItems();
  }, []);

  const confirmPurchase = async () => {
    if (!itemToConfirm) return;

    try {
      setPurchasingItemId(itemToConfirm.id);
      setLoading(true);
      closeModal(); // Cerrar modal antes de comprar

      const token = localStorage.getItem("token");

      if (!token) {
        alert("Debes iniciar sesión para comprar items");
        return;
      }

      console.log(`Comprando item ${itemToConfirm.id} con token:`, token);

      const response = await shopService.buyItem(itemToConfirm.id, token);

      console.log("Respuesta del servidor:", response);

      alert("¡Compra realizada con éxito!");

      await fetchShopItems();
      await fetchItemsByCategory();
      await fetchUserItems();
    } catch (error: any) {
      console.error("Error al comprar el item:", error);

      if (error.response?.status === 400) {
        alert("No tienes suficientes monedas para comprar este item");
      } else if (error.response?.status === 404) {
        alert("Item no encontrado");
      } else {
        alert("Error al realizar la compra. Intenta nuevamente.");
      }
    } finally {
      setLoading(false);
      setPurchasingItemId(null);
    }
  };

  const handleBuyItem = async (itemId: number) => {
    try {
      setPurchasingItemId(itemId);
      setLoading(true);

      const token = localStorage.getItem("token");

      if (!token) {
        alert("Debes iniciar sesión para comprar items");
        return;
      }

      console.log(`Comprando item ${itemId} con token:`, token);

      const response = await shopService.buyItem(itemId, token);

      console.log("Respuesta del servidor:", response);

      alert("¡Compra realizada con éxito!");

      await fetchShopItems();
      await fetchItemsByCategory();
    } catch (error: any) {
      console.error("Error al comprar el item:", error);

      if (error.response?.status === 400) {
        alert("No tienes suficientes monedas para comprar este item");
      } else if (error.response?.status === 404) {
        alert("Item no encontrado");
      } else {
        alert("Error al realizar la compra. Intenta nuevamente.");
      }
    } finally {
      setLoading(false);
      setPurchasingItemId(null);
    }
  };

  const getImagePath = (item: ShopItem) => {
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

  const getCategoryClass = (item: ShopItem) => {
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

  const getFilteredItems = (): ShopItem[] => {
    if (selectedCategory === "ALL") {
      return shopItems;
    }

    if (itemsByCategory && itemsByCategory[selectedCategory]) {
      return itemsByCategory[selectedCategory];
    }

    return [];
  };

  const categoryLabels: Record<ItemCategory | "ALL", string> = {
    ALL: "Todos",
    SKIN: "SKIN",
    HAIR: "HAIR",
    SHIRT: "SHIRT",
    BACKGROUND: "BACKGROUND",
  };

  return (
    <MainLayout title="Shop">
      <div className="container-for-scroll">
        <div className="profile-page-container">
          <section className="items-container">
            <h3>Productos Disponibles:</h3>

            {/* Botones de filtro por categoría */}
            <div className="category-filters">
              <button
                className={`filter-button ${
                  selectedCategory === "ALL" ? "active" : ""
                }`}
                onClick={() => setSelectedCategory("ALL")}
              >
                {categoryLabels.ALL}
              </button>
              <button
                className={`filter-button ${
                  selectedCategory === "SKIN" ? "active" : ""
                }`}
                onClick={() => setSelectedCategory("SKIN")}
              >
                {categoryLabels.SKIN}
              </button>
              <button
                className={`filter-button ${
                  selectedCategory === "HAIR" ? "active" : ""
                }`}
                onClick={() => setSelectedCategory("HAIR")}
              >
                {categoryLabels.HAIR}
              </button>
              <button
                className={`filter-button ${
                  selectedCategory === "SHIRT" ? "active" : ""
                }`}
                onClick={() => setSelectedCategory("SHIRT")}
              >
                {categoryLabels.SHIRT}
              </button>
              <button
                className={`filter-button ${
                  selectedCategory === "BACKGROUND" ? "active" : ""
                }`}
                onClick={() => setSelectedCategory("BACKGROUND")}
              >
                {categoryLabels.BACKGROUND}
              </button>
            </div>

            <div className="owned-shop">
              {getFilteredItems().map((item) => {
                const isOwned = userOwnsItem(item.id); // 👈 VERIFICAR
                const isPurchasing = purchasingItemId === item.id;

                return (
                  <div key={item.id} className="item-card">
                    <img
                      src={getImagePath(item)}
                      alt={item.name}
                      className={`item-image ${getCategoryClass(item)}`}
                    />
                    <p className="item-name">{item.name}</p>
                    <p className="item-price">🪙 {item.price}</p>
                    <button
                      className="buy-button"
                      onClick={() => openConfirmModal(item)}
                      disabled={isOwned || (loading && isPurchasing)} // 👈 MODIFICADO
                    >
                      {isPurchasing
                        ? "Comprando..."
                        : isOwned
                        ? "Ya lo tenes"
                        : "Comprar"}
                    </button>
                  </div>
                );
              })}
            </div>
          </section>
        </div>
      </div>
      {showModal && itemToConfirm && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <h2>Confirmar Compra</h2>
            <p>
              ¿Estás seguro de que queres comprar{" "}
              <strong>{itemToConfirm.name}</strong>?
            </p>
            <p className="modal-price">Precio: 🪙 {itemToConfirm.price}</p>

            <div className="modal-buttons">
              <button className="modal-button cancel" onClick={closeModal}>
                Cancelar
              </button>
              <button
                className="modal-button confirm"
                onClick={confirmPurchase}
              >
                Aceptar
              </button>
            </div>
          </div>
        </div>
      )}
    </MainLayout>
  );
}
