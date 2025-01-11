import dayjs from 'dayjs';
import { PaymentMethod } from 'app/shared/model/enumerations/payment-method.model';

export interface IPayment {
  id?: number;
  paymentMethod?: keyof typeof PaymentMethod | null;
  cardNumber?: string | null;
  expirationDate?: dayjs.Dayjs | null;
  cvv?: string | null;
}

export const defaultValue: Readonly<IPayment> = {};
