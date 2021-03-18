export interface IEtest {
  id?: number;
  testname?: string | null;
  testnum?: number | null;
  testaddress?: string | null;
  testphone?: string | null;
}

export class Etest implements IEtest {
  constructor(
    public id?: number,
    public testname?: string | null,
    public testnum?: number | null,
    public testaddress?: string | null,
    public testphone?: string | null
  ) {}
}
