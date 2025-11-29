import { Link } from "react-router-dom";
import { Avatar } from "../../components/layout/Avatar";
import MainLayout from "../../components/layout/MainLayout";
import { useUser } from "../../context/UserContext";
import "./Profile.css";
import UserStats from "./UserStats";

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

  const { name, surname, username, email, avatar, level } = user;

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
                  <strong>Nivel:</strong>
                  <span>
                    {level.level} - {level.name}
                  </span>
                  <img
                    src={level.iconUrl}
                    alt={level.name}
                    className="level-icon"
                  />
                </div>
              </div>
            </section>
          </div>

          <UserStats />
        </div>
      </div>
    </MainLayout>
  );
}
