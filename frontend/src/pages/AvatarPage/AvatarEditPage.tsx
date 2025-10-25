import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import { useUser } from "../../context/UserContext";
import userService, {
  type ShopItem,
  type Avatar as AvatarType,
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

  return (
    <MainLayout title="Editar Avatar">
      <div className="avatar-edit-container">
        <section className="avatar-preview-section">
          {/* <h3>Vista previa del avatar</h3> */}
          <Avatar avatar={selectedAvatar} size={250} />
        </section>

        <div className="items-scroll">
          <section className="avatar-items-section">
            {categories.map((category) => (
              <div key={category} className="category-section">
                <h4>{category.toUpperCase()}</h4>
                <div className="edit-items-grid">
                  {userItems
                    .filter((item) => item.category.toLowerCase() === category)
                    .map((item) => (
                      <div
                        key={item.id}
                        className={`selectable-item ${
                          (selectedAvatar[category] as ShopItem).id === item.id
                            ? "selected"
                            : ""
                        }`}
                        onClick={() => handleSelectItem(category, item)}
                      >
                        <OwnedItemCard item={item} />
                      </div>
                    ))}
                </div>
              </div>
            ))}
          </section>
        </div>
        <button className="save-avatar-btn" onClick={handleSave}>
          Guardar Avatar
        </button>
      </div>
    </MainLayout>
  );
}
