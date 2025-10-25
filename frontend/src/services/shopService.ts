import apiClient from "./api";
import type { ItemCategory, ShopItem } from "./userService";

export type NewShopItemDTO = {
  name: string;
  description: string;
  price: number;
};

const shopService = {
  async getAllItems(): Promise<ShopItem[]> {
    const response = await apiClient.get<ShopItem[]>("/shop");
    return response.data;
  },

  async createItem(item: NewShopItemDTO): Promise<ShopItem> {
    const response = await apiClient.post<ShopItem>("/shop", item);
    return response.data;
  },

  async buyItem(itemId: number, token: string): Promise<any> {
    const response = await apiClient.put(`/shop/buy/${itemId}`, null, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  },

  async getItemsByCategory(): Promise<Record<ItemCategory, ShopItem[]>> {
    const response = await apiClient.get<Record<ItemCategory, ShopItem[]>>("/shop/allByCategory");
    return response.data;
  }
};

export default shopService;