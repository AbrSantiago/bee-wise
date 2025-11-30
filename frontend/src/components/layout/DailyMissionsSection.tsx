import { useEffect, useState } from "react";
import dailyMissionService, {
  type DailyMissionDTO,
} from "../../services/dailyMissionService";
import "./DailyMissionsSection.css";
import MissionCard from "./MissionCard";

export default function DailyMissionsSection() {
  const [missions, setMissions] = useState<DailyMissionDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        const data = await dailyMissionService.getAll();
        setMissions(data);
      } catch (err) {
        console.error("Error loading daily missions:", err);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  return (
    <div className="daily-missions-container">
      <h2 className="missions-title">Misiones Diarias</h2>

      <div className="missions-list">
        {loading ? (
          <p>Cargando misiones diarias...</p>
        ) : (
          missions.map((m, index) => <MissionCard key={index} mission={m} />)
        )}
      </div>
    </div>
  );
}
