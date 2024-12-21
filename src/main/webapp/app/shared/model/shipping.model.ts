import { ShippingMethod } from 'app/shared/model/enumerations/shipping-method.model';

export interface IShipping {
  id?: number;
  streetAddress?: string | null;
  city?: string | null;
  state?: string | null;
  postalCode?: string | null;
  country?: string | null;
  method?: keyof typeof ShippingMethod | null;
  cost?: number | null;
}

export const defaultValue: Readonly<IShipping> = {};
