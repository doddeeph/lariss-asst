import { IProduct } from 'app/shared/model/product.model';
import { IDescription } from 'app/shared/model/description.model';
import { IColor } from 'app/shared/model/color.model';
import { IProcessor } from 'app/shared/model/processor.model';
import { IMemory } from 'app/shared/model/memory.model';
import { IStorage } from 'app/shared/model/storage.model';

export interface IProductDetails {
  id?: number;
  name?: string;
  price?: number;
  thumbnail?: string;
  product?: IProduct | null;
  description?: IDescription | null;
  color?: IColor | null;
  processor?: IProcessor | null;
  memory?: IMemory | null;
  storage?: IStorage | null;
}

export const defaultValue: Readonly<IProductDetails> = {};
