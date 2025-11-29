import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import { useUser } from "../../context/UserContext";
import userService, {
  type ShopItem,
  type Avatar as AvatarType,
  type ItemCategory,
} from "../../services/userService";
import { OwnedItemCard } from "../Profile/OwnedItemCard";
import "./AvatarEditPage.css";
import { Avatar } from "../../components/layout/Avatar";
import { useNavigate } from "react-router-dom";

export function AvatarEditPage() {
  const navigate = useNavigate();
  const { user, setUser } = useUser();
  const [userItems, setUserItems] = useState<ShopItem[]>([]);
  const [selectedAvatar, setSelectedAvatar] = useState<AvatarType | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState<
    ItemCategory | "ALL"
  >("ALL");

  useEffect(() => {
    if (!user) return;

    const token = localStorage.getItem("token") || "";

    const fetchData = async () => {
      setLoading(true);
      try {
        const items = await userService.getUserItems(token);
        setUserItems(items);
        setSelectedAvatar(user.avatar);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [user]);

  if (!user || !selectedAvatar) {
    return (
      <MainLayout title="Editar Avatar">
        <div className="avatar-edit-container">
          <p>Cargando avatar...</p>
        </div>
      </MainLayout>
    );
  }

  const categories: (keyof AvatarType)[] = [
    "skin",
    "hair",
    "shirt",
    "background",
  ];

  const categoryLabels: Record<ItemCategory | "ALL", string> = {
    ALL: "Todos",
    SKIN: "SKIN",
    HAIR: "HAIR",
    SHIRT: "SHIRT",
    BACKGROUND: "BACKGROUND",
  };

  const handleSelectItem = (category: keyof AvatarType, item: ShopItem) => {
    setSelectedAvatar({ ...selectedAvatar, [category]: item });
  };

  const handleSave = async () => {
    try {
      const updatedUser = await userService.updateAvatar(
        user.id,
        selectedAvatar
      );
      setUser(updatedUser);
      alert("Avatar actualizado con éxito!");
      navigate("/profile");
    } catch (err) {
      console.error(err);
      alert("Error al actualizar avatar.");
    }
  };

  const getFilteredItems = (): ShopItem[] => {
    if (selectedCategory === "ALL") {
      return userItems;
    }
    return userItems.filter(
      (item) => item.category.toUpperCase() === selectedCategory
    );
  };

  return (
    <MainLayout title="Editar Avatar">
      <div className="avatar-edit-container">
        <section className="avatar-preview-section">
          <Avatar avatar={selectedAvatar} size={250} />
        </section>

        <div className="items-scroll">
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

          <section className="avatar-items-section">
            <div className="category-section">
              <div className="edit-items-grid">
                {getFilteredItems().map((item) => {
                  const categoryKey =
                    item.category.toLowerCase() as keyof AvatarType;
                  return (
                    <div
                      key={item.id}
                      className={`selectable-item ${
                        (selectedAvatar[categoryKey] as ShopItem)?.id ===
                        item.id
                          ? "selected"
                          : ""
                      }`}
                      onClick={() => handleSelectItem(categoryKey, item)}
                    >
                      <OwnedItemCard item={item} />
                    </div>
                  );
                })}
              </div>
            </div>
          </section>
        </div>
        <button className="save-avatar-btn" onClick={handleSave}>
          Guardar Avatar
        </button>
      </div>
    </MainLayout>
  );
}
