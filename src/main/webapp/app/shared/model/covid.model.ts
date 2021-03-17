export interface ICovid {
  id?: number;
  cid?: number | null;
  ctype?: string | null;
  centername?: string | null;
  coi?: string | null;
  facilityname?: string | null;
  zipcode?: number | null;
  address?: string | null;
}

export class Covid implements ICovid {
  constructor(
    public id?: number,
    public cid?: number | null,
    public ctype?: string | null,
    public centername?: string | null,
    public coi?: string | null,
    public facilityname?: string | null,
    public zipcode?: number | null,
    public address?: string | null
  ) {}
}
