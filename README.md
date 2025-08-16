# bee-wise

# Tecnologías utilizadas

Java 17
React 18
Postgresql 17.5-alpine3.22


# BD Postgres (con Docker)

docker run --name bee-wise -e POSTGRES_USER=dockerdb -e POSTGRES_PASSWORD=beewise2025 -e POSTGRES_DB=bee-wise -p 5432:5432 -d postgres:17.5-alpine3.22 

# Frontend (React JS + TypeScript)

Para ejecutar el frontend:

1. Navegar a la carpeta del frontend:
   ```bash
   cd frontend
   ```

2. Instalar las dependencias:
   ```bash
   npm install
   ```

3. Ejecutar el proyecto:
   ```bash
   npm start
   ```

La aplicación se ejecutará en http://localhost:3000