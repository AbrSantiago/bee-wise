import api from './api';

export const testConnection = async () => {
  try {
    const response = await api.get('/test/hello'); // Cambiar la URL según tu configuración
    return response.data;
  } catch (error) {
    throw error;
  }
};