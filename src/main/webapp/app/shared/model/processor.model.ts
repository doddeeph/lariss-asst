export interface IProcessor {
  id?: number;
  name?: string;
  value?: string | null;
}

export const defaultValue: Readonly<IProcessor> = {};
