import { ICategory } from 'app/shared/model/category.model';

export interface IProduct {
  id?: number;
  name?: string;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IProduct> = {};
