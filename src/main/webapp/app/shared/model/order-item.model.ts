import { IOrder } from 'app/shared/model/order.model';
import { IProductDetails } from 'app/shared/model/product-details.model';

export interface IOrderItem {
  id?: number;
  quantity?: number | null;
  totalPrice?: number | null;
  order?: IOrder | null;
  productDetails?: IProductDetails | null;
}

export const defaultValue: Readonly<IOrderItem> = {};
