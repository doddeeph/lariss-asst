import dayjs from 'dayjs';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IOrder {
  id?: number;
  status?: keyof typeof OrderStatus;
  createdDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IOrder> = {};
