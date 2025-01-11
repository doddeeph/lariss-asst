import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customer.model';
import { IShipping } from 'app/shared/model/shipping.model';
import { IBilling } from 'app/shared/model/billing.model';
import { IPayment } from 'app/shared/model/payment.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IOrder {
  id?: number;
  status?: keyof typeof OrderStatus | null;
  totalPrice?: number | null;
  trackId?: string | null;
  orderDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  customer?: ICustomer | null;
  shipping?: IShipping | null;
  billing?: IBilling | null;
  payment?: IPayment | null;
}

export const defaultValue: Readonly<IOrder> = {};
