import { IOrder } from 'app/shared/model/order.model';
import { IProductDetails } from 'app/shared/model/product-details.model';

export interface IOrderProduct {
  id?: number;
  quantity?: number;
  order?: IOrder | null;
  productDetails?: IProductDetails | null;
}

export const defaultValue: Readonly<IOrderProduct> = {};
