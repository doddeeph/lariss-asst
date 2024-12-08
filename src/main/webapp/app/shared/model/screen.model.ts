export interface IScreen {
  id?: number;
  name?: string;
  value?: string | null;
}

export const defaultValue: Readonly<IScreen> = {};
