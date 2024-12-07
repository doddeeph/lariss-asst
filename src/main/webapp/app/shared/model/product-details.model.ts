import { IProduct } from 'app/shared/model/product.model';
import { IDescription } from 'app/shared/model/description.model';
import { IColor } from 'app/shared/model/color.model';
import { IProcessor } from 'app/shared/model/processor.model';
import { IMemory } from 'app/shared/model/memory.model';
import { IStorage } from 'app/shared/model/storage.model';
import { IScreen } from 'app/shared/model/screen.model';
import { IConnectivity } from 'app/shared/model/connectivity.model';
import { IMaterial } from 'app/shared/model/material.model';
import { ICaseSize } from 'app/shared/model/case-size.model';
import { IStrapColor } from 'app/shared/model/strap-color.model';
import { IStrapSize } from 'app/shared/model/strap-size.model';

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
  screen?: IScreen | null;
  connectivity?: IConnectivity | null;
  material?: IMaterial | null;
  caseSize?: ICaseSize | null;
  strapColor?: IStrapColor | null;
  strapSize?: IStrapSize | null;
}

export const defaultValue: Readonly<IProductDetails> = {};
