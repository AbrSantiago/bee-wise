import { useEffect, useState } from "react";
import api from "./../services/api";

function Test() {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const testEndpoints = async () => {      
      // Probar /test/hello
      try {
        const helloResponse = await api.get("/test/hello");
        console.log("Endpoint de /test/hello:", helloResponse.data);
      } catch (error) {
        console.error("❌ Error en /test/hello:", error);
      }

      // Probar /test/status
      try {
        const statusResponse = await api.get("/test/status");
        console.log("Endpoint de /test/status:", statusResponse.data);
      } catch (error) {
        console.error("❌ Error en /test/status:", error);
      }
      setIsLoading(false);
    };

    testEndpoints();
  }, []);

  return (
    <div>
      <h2>Pruebas de Endpoints</h2>
    </div>
  );
}

export default Test;
