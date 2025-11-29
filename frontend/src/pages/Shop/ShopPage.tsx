import type { ShopItem, ItemCategory } from "../../services/userService";
import shopService from "../../services/shopService";
import "./ShopPage.css";
import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import { useUser } from "../../context/UserContext";
import { ShopItemCard } from "./ShopItemCard";

export function ShopPage() {
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
  const [userItems, setUserItems] = useState<ShopItem[]>([]);
  const { user, setUser } = useUser();

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
    } catch (error) {
      console.error("Error al traer los items de la tienda:", error);
    }
  };

  const fetchItemsByCategory = async () => {
    try {
      const items = await shopService.getItemsByCategory();
      setItemsByCategory(items);
    } catch (error) {
      console.error("Error al traer items por categoría:", error);
    }
  };

  const userOwnsItem = (itemId: number): boolean => {
    return userItems.some((userItem) => userItem.id === itemId);
  };

  useEffect(() => {
    if (!user) return;
    fetchShopItems();
    fetchItemsByCategory();
    setUserItems(user.items || []);
  }, [user]);

  const confirmPurchase = async () => {
    if (!itemToConfirm) return;

    try {
      setPurchasingItemId(itemToConfirm.id);
      setLoading(true);
      closeModal();

      const token = localStorage.getItem("accessToken");

      if (!token || !user) {
        alert("Debes iniciar sesión para comprar items");
        return;
      }

      const response = await shopService.buyItem(itemToConfirm.id, token);

      alert("¡Compra realizada con éxito!");

      setUser(response);

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
              {getFilteredItems().map((item) => (
                <ShopItemCard
                  key={item.id}
                  item={item}
                  userOwnsItem={userOwnsItem}
                  purchasingItemId={purchasingItemId}
                  loading={loading}
                  openConfirmModal={openConfirmModal}
                />
              ))}
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
