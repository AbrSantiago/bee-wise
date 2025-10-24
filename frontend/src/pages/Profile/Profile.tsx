import MainLayout from "../../components/layout/MainLayout";
import { useUser } from "../../context/UserContext";
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

  const { name, surname, username, email, points, beeCoins, avatar } = user;

  return (
    <MainLayout title="Perfil">
      <div className="profile-container">
        {/* Datos del usuario */}
        <section className="profile-info">
          <h2>{username}</h2>
          <p>
            {name} {surname}
          </p>
          <p>{email}</p>
          <div className="profile-stats">
            <div>
              <strong>Puntos:</strong> {points}
            </div>
            <div>
              <strong>BeeCoins:</strong> 🪙 {beeCoins}
            </div>
          </div>
        </section>

        {/* Avatar */}
        <section className="profile-avatar">
          <h3>Tu avatar</h3>
          <div className="avatar-preview">
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

          <button className="edit-avatar-btn">Editar avatar</button>
        </section>
      </div>
    </MainLayout>
  );
}
