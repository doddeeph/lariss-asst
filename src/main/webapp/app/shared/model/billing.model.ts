export interface IBilling {
  id?: number;
  streetAddress?: string | null;
  city?: string | null;
  state?: string | null;
  postalCode?: string | null;
  country?: string | null;
}

export const defaultValue: Readonly<IBilling> = {};
