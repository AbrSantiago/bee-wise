import { Link } from "react-router-dom";
import { Avatar } from "../../components/layout/Avatar";
import MainLayout from "../../components/layout/MainLayout";
import { useUser } from "../../context/UserContext";
import { OwnedItemCard } from "./OwnedItemCard";
import "./Profile.css";

export function ProfilePage() {
  const { user } = useUser();

  if (!user) {
    return (
      <MainLayout title="Perfil">
        <div className="profile-container">
          <p>Cargando perfil...</p>
        </div>
      </MainLayout>
    );
  }

  const { name, surname, username, email, beeCoins, avatar, items } = user;

  return (
    <MainLayout title="Perfil">
      <div className="container-for-scroll">
        <div className="profile-page-container">
          <div className="profile-container">
            {/* Avatar */}
            <section className="profile-avatar">
              <Avatar avatar={avatar} size={250} />
              <Link to={"/avatar"}>
                <button className="edit-avatar-btn">Editar avatar</button>
              </Link>
            </section>

            {/* Datos del usuario */}
            <section className="profile-info">
              <h2>{username}</h2>
              <p>
                {name} {surname}
              </p>
              <p>{email}</p>
              <div className="profile-stats">
                <div>
                  <strong>BeeCoins:</strong> 🪙 {beeCoins}
                </div>
              </div>
            </section>
          </div>

          {/* Items del usuario */}
          <section className="user-items-container">
            <h3>Tus ítems</h3>
            {items.length === 0 ? (
              <p className="no-items">Todavía no compraste ningún ítem</p>
            ) : (
              <div className="items-grid">
                {items.map((item) => (
                  <OwnedItemCard key={item.id} item={item} />
                ))}
              </div>
            )}
          </section>
        </div>
      </div>
    </MainLayout>
  );
}
