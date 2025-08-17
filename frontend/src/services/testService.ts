import api from './api';

export const testConnection = async () => {
  try {
    const response = await api.get('/api/test/hello');
    return response.data;
  } catch (error) {
    throw error;
  }
};