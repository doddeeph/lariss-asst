export interface IDescription {
  id?: number;
  name?: string;
  value?: string | null;
}

export const defaultValue: Readonly<IDescription> = {};
