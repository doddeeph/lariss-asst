export interface IStorage {
  id?: number;
  name?: string;
  value?: string | null;
}

export const defaultValue: Readonly<IStorage> = {};
