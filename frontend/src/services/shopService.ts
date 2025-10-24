import apiClient from "./api";

export type ShopItemDTO = {
  id: number;
  name: string;
  description: string;
  price: number;
};

export type NewShopItemDTO = {
  name: string;
  description: string;
  price: number;
};

const shopService = {
  async getAllItems(): Promise<ShopItemDTO[]> {
    const response = await apiClient.get<ShopItemDTO[]>("/shop");
    return response.data;
  },

  async createItem(item: NewShopItemDTO): Promise<ShopItemDTO> {
    const response = await apiClient.post<ShopItemDTO>("/shop", item);
    return response.data;
  },

  async buyItem(itemId: number, token: string): Promise<any> {
    const response = await apiClient.put(`/shop/buy/${itemId}`, null, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  }
};

export default shopService;